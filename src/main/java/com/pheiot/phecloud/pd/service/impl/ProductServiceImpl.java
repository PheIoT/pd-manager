package com.pheiot.phecloud.pd.service.impl;

import com.google.common.collect.Lists;
import com.pheiot.bamboo.common.utils.mapper.BeanMapper;
import com.pheiot.phecloud.pd.dao.ProductDao;
import com.pheiot.phecloud.pd.dto.ProductDto;
import com.pheiot.phecloud.pd.entity.Product;
import com.pheiot.phecloud.pd.service.ProductService;
import com.pheiot.phecloud.pd.utils.ApplicationException;
import com.pheiot.phecloud.pd.utils.ExceptionCode;
import com.pheiot.phecloud.pd.utils.KeyGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    protected ProductDao productDao;

    @Override
    @Transactional(readOnly = true)
    public ProductDto findProductById(Long id) {
        if (id == null) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }

        Optional<Product> product = productDao.findById(id);

        if (product.isPresent()) {
            throw new ApplicationException(ExceptionCode.OBJECT_NOT_FOUND);
        }

        ProductDto productDto = BeanMapper.map(product.get(), ProductDto.class);

        return productDto;
    }

    @Transactional
    public ProductDto save(ProductDto productDto) {
        if (productDto == null) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }

        Product product = BeanMapper.map(productDto, Product.class);
        product.setPkey(KeyGenerator.generateKey());
        product.setSecret(KeyGenerator.generateSecret());

        productDao.save(product);

        ProductDto dto = BeanMapper.map(product, ProductDto.class);
        logger.info("Save product:{}", productDto.getDisplayName());

        return dto;
    }


    @Override
    public void update(ProductDto productDto) {
        if (productDto == null || StringUtils.isBlank(productDto.getPkey())) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }
        Product product = productDao.findByPkey(productDto.getDisplayName());

        if (product == null) {
            throw new ApplicationException(ExceptionCode.OBJECT_NOT_FOUND);
        }

        product = BeanMapper.map(productDto, BeanMapper.getType(ProductDto.class), BeanMapper.getType(Product.class));

        productDao.save(product);

        logger.info("Update product:{}", productDto.getDisplayName());
    }

    @Override
    public void changeEnabledTo(String key, boolean isEnabled) {
        if (StringUtils.isBlank(key)) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }

        Product product = productDao.findByPkey(key);

        if (product == null) {
            throw new ApplicationException(ExceptionCode.OBJECT_NOT_FOUND);
        }

        product.setIsEnabled(isEnabled);

        productDao.save(product);

        logger.info("Change enabled to {} for product:{}", isEnabled, product.getDisplayName());
    }

    @Override
    public ProductDto findProductByKey(String key) {
        if (StringUtils.isBlank(key)) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }

        Product product = productDao.findByPkey(key);

        if (product == null) {
            throw new ApplicationException(ExceptionCode.OBJECT_NOT_FOUND);
        }

        ProductDto dto = BeanMapper.map(product, ProductDto.class);

        return dto;
    }

    @Override
    public List<ProductDto> findProductByUserKey(String userKey) {
        if (StringUtils.isBlank(userKey)) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }

        List<Product> list = productDao.findByUkey(userKey);

        List<ProductDto> dtoList = Lists.newArrayList();

        for (Product entity : list) {
            ProductDto dto = BeanMapper.map(entity, ProductDto.class);
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }

        Optional<Product> product = productDao.findById(id);

        if (product.isPresent()) {
            throw new ApplicationException(ExceptionCode.PARAMTER_ERROR);
        }

        productDao.deleteById(id);
        logger.info("Delete product:{}", product.get().getDisplayName());
    }
}
