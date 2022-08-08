package org.novinomad.picasso.configurations;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.novinomad.picasso.services.ILayoutSideToggleHolder;
import org.novinomad.picasso.services.impl.LayoutSideToggleHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class WebConfig {

    @Bean(name = "sideNavVisibilityHolder")
    @Scope(value = WebApplicationContext.SCOPE_SESSION,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ILayoutSideToggleHolder sideNavToggled() {
        return new LayoutSideToggleHolder();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
