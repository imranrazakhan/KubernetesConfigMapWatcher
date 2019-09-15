package com.imran.kubernetes.configmap;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;


public final class ConfigMapWatchClient {
	
	public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.bind("trustCert", new HttpClientConfigurerTrustAllCACerts());
        main.addRouteBuilder(new ConfigMapWatchClientRouter());
        main.run(args);
    }
	
	private static class ConfigMapWatchClientRouter extends RouteBuilder {
        @Override
        public void configure() throws Exception {
             from("timer://foo?fixedRate=true&period=60000")
             .setHeader("Authorization", simple("Bearer sXoZfVfMW2f4nWjiFsOGjf56B70nEjr18LX1c4bHHVo"))
             .setHeader("Accept", simple("application/json"))
             .setHeader("CamelHttpMethod", constant("GET"))
             .to("https4://m.devokd.younicos.local:8443/api/v1/watch/namespaces/yq-qa/configmaps/my-config-map?httpClientConfigurer=#trustCert")
             .to("log:my?showAll=true&multiline=true")
             .to("mock:result");
        }
	}

}
