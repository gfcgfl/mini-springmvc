package cc.gfc.mvc.application.service;


import cc.gfc.mvc.framework.annotation.MyBean;

/**
 * @author xiaoguo
 * @ClassName: SalaryService
 * @Description:
 * @CreatedAt: 6/21/19 11:34 AM
 **/
@MyBean
public class SalaryService {
    public Integer getSalaryById(Integer id){
        return id * 100;
    }
}
