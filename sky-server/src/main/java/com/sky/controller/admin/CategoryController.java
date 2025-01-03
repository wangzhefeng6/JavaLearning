package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 这里有一个非常需要注意的地方就是，SpringBoot项目启动失败，Ambiguous mapping. Cannot map ‘xxxController‘ method
     * 这个问题是因为：模糊的方法/路径匹配，XxxController.workListStatistics()这个方法和YyyController.workListStatistics()的方法都配置了一样的访问路径。
     * 在这里就是update和save函数提供了相同的访问路径
     * 就是说
     * 因为这两个在接口文档中都是POST形式的请求，所以我刚开始使用的是都是PostMapping
     * 将其中一个修改为PutMapping后即可
     */

    @PutMapping
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody  CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return  Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}",categoryPageQueryDTO);
        PageResult pageResult=categoryService.pageQuery(categoryPageQueryDTO);
        return  Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result<String> startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用员工账号,{},{}",status,id);
        categoryService.startOrStop(status,id);
        return  Result.success();
    }
    @PostMapping
    @ApiOperation("新增分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}",categoryDTO);
        categoryService.save(categoryDTO);
        return  Result.success();
    }
    @ApiOperation("删除对应id")
    @DeleteMapping
    public Result<String> deleteById(Long id) {
        log.info("删除id是:{}",id);
        categoryService.deleteById(id);
        return  Result.success();
    }
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
          List<Category> categories=categoryService.list(type);
          return  Result.success(categories);
    }

//    @ApiOperation("查询分类")
//    @GetMapping("/list")
//    public
}
