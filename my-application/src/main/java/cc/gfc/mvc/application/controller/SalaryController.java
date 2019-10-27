package cc.gfc.mvc.application.controller;


import cc.gfc.mvc.application.pojo.User;
import cc.gfc.mvc.application.service.SalaryService;
import cc.gfc.mvc.framework.annotation.MyAutoWired;
import cc.gfc.mvc.framework.annotation.MyController;
import cc.gfc.mvc.framework.annotation.MyRequestMapping;
import cc.gfc.mvc.framework.annotation.MyRequestParam;

/**
 * @author xiaoguo
 * @ClassName: SalaryController
 * @Description:
 * @CreatedAt: 6/19/19 7:32 PM
 **/
@MyController
public class SalaryController {
    @MyAutoWired
    private SalaryService salaryService;

    @MyRequestMapping("/salary")
    public Integer getSalaryByNameAndDegree(@MyRequestParam("name") String name, @MyRequestParam("degree") String degree) {
        if ("xiaoming".equals(name) && "master".equals(degree)) {
            return 1000;
        } else {
            return 500;
        }
    }

    @MyRequestMapping("/salary/modify")
    public User modifySalary(String name, String salary) {
        return new User("xxx", "1000");
    }

    @MyRequestMapping("/salary/id")
    public Integer getSalaryById(@MyRequestParam("id") String id) {
//        System.out.println(id);
        return salaryService.getSalaryById(Integer.parseInt(id));
    }
}
