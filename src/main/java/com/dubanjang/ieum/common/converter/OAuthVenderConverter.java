package com.dubanjang.ieum.common.converter;

import com.dubanjang.ieum.auth.domain.OAuthVender;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OAuthVenderConverter implements Converter<String, OAuthVender> {
    @Override
    public OAuthVender convert(String s) {
        return OAuthVender.valueOf(s.toUpperCase());
    }
}
