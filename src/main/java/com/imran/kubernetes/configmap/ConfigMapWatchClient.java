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
        	
        	String host = "https://localhost:8443";
            String authToken = "xxxxx";
        	 
        	fromF("kubernetes-config-maps://%s?oauthToken=%s&trustCerts=true&namespace=yq-qa&resourceName=my-config-map", host, authToken)
            .choice()
                .when( header("CamelKubernetesEventAction").isEqualTo("MODIFIED") )
                	.to("direct:restartPods")
            .end();
            
            
            from("direct:restartPods")
            .toF("kubernetes-pods://%s?oauthToken=%s&trustCerts=true&namespace=default&operation=listPodsByLabels&labelKey=this&labelValue=rocks", host, authToken)
            .split(body())
                .setHeader(KubernetesConstants.KUBERNETES_POD_NAME, simple("${body.getMetadata().getName()}"))
                .toF("kubernetes-pods://%s?oauthToken=%s&trustCerts=true&operation=deletePod", host, authToken)
                .choice()
                    .when( body() )
                        .log(simple("${header.CamelKubernetesPodName}")+" has been restarted.")
                .end()
            .end()
            .log("#################ALL PODs sas been Restarted##################");
           
        }
	}

}
