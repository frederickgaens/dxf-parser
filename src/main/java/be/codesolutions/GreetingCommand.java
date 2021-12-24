package be.codesolutions;

import be.codesolutions.dxf.DxfDocument;
import be.codesolutions.resolve.DxfRvlFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@QuarkusMain
public class GreetingCommand implements Runnable, QuarkusApplication {

    @Override
    public int run(String... args) throws Exception {
        new GreetingCommand().run();
        return 0;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File("/home/frederick/Downloads/dxf.json")))) {
//        String dxf = "/home/var_rain/files/libdxfrw/dwg2dxf/test.dxf";
            String dxf = "/home/frederick/Downloads/1262_1040_R01_Va.dxf";
//        String dxf = "/home/var_rain/files/news.dxf";
            DxfRvlFile reso = new DxfRvlFile();
            DxfDocument document = reso.resolve(dxf);
//        System.out.println(new Gson().toJson(document));
            Gson json = new GsonBuilder().setPrettyPrinting().create();
            writer.write(json.toJson(document));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Options was ok. use time: " + (System.currentTimeMillis() - time) + "ms");
    }
}
