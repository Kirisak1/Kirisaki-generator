import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeMarkerTest {
    @Test
    public void DynamicTest() throws IOException, TemplateException {
        //配置freeMarker
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        configuration.setDefaultEncoding("utf-8");
        configuration.setNumberFormat("0.######");
        Template template = configuration.getTemplate("web.html.ftl");
        //新增数据
        HashMap<String, Object> data = new HashMap<>();
        data.put("currentYear", 2023);
        List<Map<String,String>> menuItems = new ArrayList<>();
        HashMap<String, String> menuItem1 = new HashMap<>();
        menuItem1.put("url", "https://codefather.cn");
        menuItem1.put("label", "编程导航");
        HashMap<String, String> menuItem2 = new HashMap<>();
        menuItem2.put("url", "https://laoyujianli.com");
        menuItem2.put("label", "老鱼简历");
        menuItems.add(menuItem1);
        menuItems.add(menuItem2);
        //忘记传入集合
        data.put("menuItems", menuItems);
        //生成数据
        FileWriter writer = new FileWriter("myweb.html");
        template.process(data, writer);
        //关流
        writer.close();
    }
}
