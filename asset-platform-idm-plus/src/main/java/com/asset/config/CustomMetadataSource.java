package com.asset.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * Created by hjhu on 2019/5/27.
 */
@Component
public class CustomMetadataSource implements FilterInvocationSecurityMetadataSource {

//    @Autowired
//    ResourceService resourceService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        /*List<Resource> allResources = resourceService.getAllResource();
        for (Resource resource : allResources) {
            if (antPathMatcher.match(resource.getUrl(), requestUrl)
                    &&resource.getSceneRoles().size()>0) {
                List<SceneRole> sceneRoles = resource.getSceneRoles();
                *//*System.out.println(sceneRoles.size());
                for (SceneRole role : sceneRoles) {
                    System.out.println(role.toString());
                }*//*
                int size = sceneRoles.size();
                String[] values = new String[size];
                for (int i = 0; i < size; i++) {
                    values[i] = sceneRoles.get(i).getRoleName();
                }
                return SecurityConfig.createList(values);
            }
        }*/
        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
