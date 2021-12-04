package com.bnyte.forge.springboot.filter.exclude;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @auther bnyte
 * @date 2021-12-04 22:05
 * @email bnytezz@gmail.com
 */
public class EnableForgeExclude implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        String className = metadataReader.getClassMetadata().getClassName();
        return className.endsWith("package-info");
    }
}
