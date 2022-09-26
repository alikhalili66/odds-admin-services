package ir.khalili.products.odds.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import io.vertx.ext.web.handler.StaticHandler;
import ir.khalili.products.odds.core.helper.HelperInitial;

public class EntryPoint extends AbstractVerticle {

    public static Vertx vertx;
    public static JsonObject joConfig;
    private static Logger logger = LogManager.getLogger(EntryPoint.class);
    private static Router router;
    private static int port;

    public static void main(String[] args) {

        logger.info("STARTING ......");

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/config.json")) {

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(
                    new InputStreamReader(in, Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }

            String jsonString = textBuilder.toString();
            logger.info("jsonString:" + jsonString);

            joConfig = new JsonObject(jsonString);

        } catch (Exception e) {
            logger.error("configNotValid:", e);
            System.exit(0);
        }

        try {
            port = joConfig.getInteger("port");
        } catch (Exception e) {
            logger.error("invalidConfig:", e);
            System.exit(0);
        }

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(1);

        vertx = Vertx.vertx();
        vertx.deployVerticle(EntryPoint.class.getName(), deploymentOptions, resHandler -> {

            if (resHandler.failed()) {
                logger.error("deployVerticleFailed:", resHandler.cause());
                vertx.close();
                System.exit(0);
            }

            HelperInitial.initialize(vertx).onComplete(handler -> {

                if (handler.failed()) {
                    logger.error("HelperInitial.initialize.Failed:", handler.cause());
                    vertx.close();
                    System.exit(0);
                }

                deployVerticle();

            });
        });

    }

    private static void deployVerticle() {

        //AUTH


        //COMPETITION

    	
    	
		//FOLDER
		

    	
		//GROUP
		
    	
    	
		//LEAGUE
    	
    	
    	
    	//ODDS
    	
    	
    	
    	//QUESTION
    	
    	
    	
    	//TEAM
		
    	
    	
		//USER
//		vertx.deployVerticle(VRTCL_01_UserSendOtp.class.getName());
		
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        /*********************************************************/
        /*******************Router********************************/
        /*********************************************************/
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());
        router.route().handler(ResponseTimeHandler.create());

        /*********************************************************/
        /*******************RouteManager**************************/
        /*********************************************************/


        //AUTH


        //COMPETITION

    	
    	
		//FOLDER
		

    	
		//GROUP
		
    	
    	
		//LEAGUE
    	
    	
    	
    	//ODDS
    	
    	
    	
    	//QUESTION
    	
    	
    	
    	//TEAM
		
    	
    	
		//USER
//        router.post		("/v1/service/nas/user/send/otp")						.handler(RtMgr_01_UserSendOtp							::handler);


        vertx.createHttpServer().requestHandler(router).listen(port);

        /*********************************************************/
        /*********************************************************/

        startPromise.complete();
    }

}
