package tool;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by yt on 2016/12/21.
 */
public class CreateCode {

    static final  String controllerStr="G:/Github/Project3/src/main/java/just/yt/controller/";
    static final String serviceStr="G:/Github/Project3/src/main/java/just/yt/service/";

    public static void main(String[] args) throws FileNotFoundException {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "G:\\Github\\Project3\\src\\main\\java");

        ve.init();
        String[]strsUp={"Commodity","Shoppinglist","Promotion","Comment"};
        String[]strsLow=new String[strsUp.length];
        for (int i = 0; i < strsUp.length; i++) {
            strsLow[i]=(strsUp[i].substring(0,1).toLowerCase()+strsUp[i].substring(1,strsUp[i].length()));
        }

        Template controller = ve.getTemplate("tool/controller.vm");
        Template service =ve.getTemplate("tool/service.vm");
        for (int i = 0; i <strsLow.length ; i++) {
            VelocityContext ctx = new VelocityContext();
            ctx.put("modelUp", strsUp[i]);
            ctx.put("modelLow",strsLow[i]);
            StringWriter conw = new StringWriter();
            StringWriter serw=new StringWriter();
            controller.merge(ctx, conw);
            service.merge(ctx,serw);
            writer(controllerStr+strsUp[i]+"Controller.java",conw);
            writer(serviceStr+strsUp[i]+"Service.java",serw);
        }
    }

    static void writer(String des,StringWriter sw){
        FileOutputStream of = null;
        try {
            of = new FileOutputStream(des);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            of.write(sw.toString().getBytes("UTF-8"));
            of.flush();
            of.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
