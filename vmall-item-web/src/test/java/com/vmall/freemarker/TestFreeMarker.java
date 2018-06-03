package com.vmall.freemarker;

import com.vmall.item.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreeMarker {
    @Test
    public void testFreeMarker() throws Exception {
        //1 创建一个模板文件
        //2 创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3 设置模板所在的路径
        configuration.setDirectoryForTemplateLoading(new File("/Users/xinxing/myProjects/VMall/vmall-item-web/src/main/webapp/WEB-INF/ftl"));
        //4 需要设置模板的字符集，一般是utf-8
        configuration.setDefaultEncoding("utf-8");
        //5 使用Configuration对象加载一个模板文件，需要指定模板文件的文件名
//        Template template = configuration.getTemplate("hello.ftl");
        Template template = configuration.getTemplate("student.ftl");
        //6 创建一个数据集，可以是pojo也可以是map，推荐使用map
        Map data = new HashMap<>();
        data.put("hello", "hello freemarker");
        Student student = new Student(1,"小明",10,"北京");
        data.put("student", student);
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"小明1",10,"北京"));
        studentList.add(new Student(2,"小明2",11,"北京"));
        studentList.add(new Student(3,"小明3",12,"北京"));
        studentList.add(new Student(4,"小明4",13,"北京"));
        studentList.add(new Student(5,"小明5",14,"北京"));
        studentList.add(new Student(6,"小明6",15,"北京"));
        studentList.add(new Student(7,"小明7",16,"北京"));
        data.put("studentList", studentList);
        //日期类型的处理
        data.put("date", new Date());
        data.put("val","123456");
        //7 创建一个Writer对象，指定输出文件的路径及文件名
        Writer out = new FileWriter(new File("/Users/xinxing/Desktop/out/student.html"));
        //8 使用模板对象的process方法输出文件
        template.process(data, out);
        //9 关闭流
        out.close();
    }
}
