package com.zoe.demo.controller;

import com.zoe.demo.common.ResultData;
import com.zoe.demo.entity.SysPermissionDO;
import com.zoe.demo.entity.SysRoleDO;
import com.zoe.demo.entity.dto.PermissionDTO;
import com.zoe.demo.entity.vo.PermissionVO;
import com.zoe.demo.service.SysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

/**
 * Created by 陈亚兰 on 2018/4/19.
 */
@RestController
@RequestMapping("/sys/permission")
@Api(description = "权限")
public class SysPermissionController {
    @Autowired
    private SysService sysService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiImplicitParam(name="permissionDTO",value = "权限",dataType = "PermissionDTO",paramType = "body")
    public ResultData addPermission(@RequestBody PermissionDTO permissionDTO){
        SysPermissionDO sysPermissionDO=new SysPermissionDO(permissionDTO.getPermissionCN(),permissionDTO.getPermissionEN());
        if(sysService.findByPermissionCNAndPermissionEN(sysPermissionDO)){
            return ResultData.success("添加成功");
        }else{
            return ResultData.error("存在同名权限");
        }
    }


    @ApiOperation("权限分页")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "当前页",defaultValue = "0",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name="size",value = "每页个数",defaultValue = "10",dataType = "Integer",paramType = "query")
    })
    public ResultData getPagePermission( @ApiIgnore@PageableDefault(value = 15, sort = { "id" }, direction = Sort.Direction.DESC)Pageable pageable){
        return ResultData.success(sysService.getPermissionPage(pageable));
    }

    @ApiOperation("权限更改")
    @PutMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "权限id",paramType = "path",dataType = "Long"),
            @ApiImplicitParam(name="permissionEN",value = "权限英文",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="permissionCN",value = "权限中文",paramType = "query",dataType = "String")
    })
    public ResultData updatePermission(@PathVariable Long id,String permissionEN,String permissionCN){
        SysPermissionDO permissionDO=sysService.findByPermissionId(id);
        permissionDO.setPermissionCN(permissionCN);
        permissionDO.setPermissionEN(permissionEN);
        sysService.add(permissionDO);
        return ResultData.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("权限删除")
    @ApiImplicitParam(name = "id",value = "权限id",paramType = "path",dataType = "Long")
    public ResultData deletePermission(@PathVariable Long id){
        SysPermissionDO permissionDO=sysService.findByPermissionId(id);
        List<SysRoleDO> roles=sysService.getRoleList();
        for(SysRoleDO r:roles){
            Set<SysPermissionDO> per=r.getPermissions();
            for(SysPermissionDO p:per){
                if(p.equals(permissionDO)){
                    return ResultData.error("该权限有角色在使用");
                }
            }
        }
        permissionDO.setDeleted(true);
        sysService.add(permissionDO);
        return ResultData.success("删除成功");
    }

//    @GetMapping("/getPermissionList")
//    public ResultData getList(){
//        return ResultData.success(sysService.getPermissionList());
//    }
}
