package com.sky.controller.admin;

import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
    /**
     * 退出
     * requesrBody是指的json文件的形式
     * 不需要返回参数所以Result不需要指定泛型
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        System.out.println("当前线程的id："+Thread.currentThread().getId());
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return  Result.success();
    }
    /**
     * 退出
     * requesrBody是指的json文件的形式，而这里EmployeePageQueryDTO 是Qyery数据
     * 不需要返回参数所以Result不需要指定泛型
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
       log.info("员工分页查询，参数为：{}",employeePageQueryDTO);
       PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }
    /**
     * 退出
     {status}路劲参数
     @PathVariable Integer status这里的形参需要与花括号的内容保持一致，否则需指定@PathVariable({status})
     *
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用员工账号,{},{}",status,id);
        employeeService.startOrStop(status,id);
          return  Result.success();
    }
    /**
     *
    根据id查询员工信信息
      * @return
     */
    @ApiOperation("根据id查询员工信息")
    @GetMapping("/{id}")
    public Result<Employee> getByid(@PathVariable Long id){
        Employee employee=employeeService.getByid(id);
        employee.setPassword("*****");
        return  Result.success(employee);
    }
    /**
     *
     保存修改的员工信息
     *
     * @return
     */
    @PutMapping
    @ApiOperation("保存修改的员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
