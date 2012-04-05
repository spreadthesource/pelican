package com.spreadthesource.pelican.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;

public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(StartupService.class, StartupServiceImpl.class).preventReloading().eagerLoad();
    }
    
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration)
    {
		//configuration.add(HornetSymbols.HORNET_URI, "http://yourServerHere:8187");
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
    }

    @Contribute(MarkupRenderer.class)
    public static void deactiveDefaultCSS(OrderedConfiguration<MarkupRendererFilter> configuration)
    {
        configuration.override("InjectDefaultStylesheet", null);
    }
}
