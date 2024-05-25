package com.vector.auto.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/products/**")
                .addResourceLocations("classpath:/images/products/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, MultipartFile>() {
            @Override
            public MultipartFile convert(String source) {
                if (source.trim().isEmpty()) {
                    return null;
                }

                return new MultipartFile() {
                    @Override
                    public String getContentType() {

                        return "String";
                    }

                    @Override
                    public String getName() {
                        return source;
                        
                    }

                    @Override
                    public String getOriginalFilename() {
                        String[] s = source.split("/");
                        return s[s.length-1];
                    }

                    @Override
                    public boolean isEmpty() {
                        return source.trim().isEmpty();
                    }

                    @Override
                    public long getSize() {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getSize'");
                    }

                    @Override
                    public byte[] getBytes() throws IOException {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getBytes'");
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'getInputStream'");
                    }

                    @Override
                    public void transferTo(File dest) throws IOException, IllegalStateException {
                        // TODO Auto-generated method stub
                        throw new UnsupportedOperationException("Unimplemented method 'transferTo'");
                    }
                };
            }
        });
    }
}
