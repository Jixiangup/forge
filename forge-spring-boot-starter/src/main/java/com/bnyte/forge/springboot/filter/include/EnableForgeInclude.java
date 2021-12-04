package com.bnyte.forge.springboot.filter.include;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @auther bnyte
 * @date 2021-12-04 22:06
 * @email bnytezz@gmail.com
 */
public class EnableForgeInclude implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return true;
    }
}
