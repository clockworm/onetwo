

package org.onetwo.plugins.admin.controller;

import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyDataGrid;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.AdminModule.AdminRoleMgr;
import org.onetwo.plugins.admin.entity.AdminRole;
import org.onetwo.plugins.admin.service.impl.AdminRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("role")
public class AdminRoleController extends WebAdminBaseController {

    @Autowired
    private AdminRoleServiceImpl adminRoleServiceImpl;
    
    
    @ByPermissionClass(AdminRoleMgr.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyDataGrid<AdminRole> easyPage, AdminRole adminRole){
        return responsePageOrData("/admin-role-index", ()->{
        			Page<AdminRole> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
                    adminRoleServiceImpl.findPage(page, adminRole);
                    return EasyDataGrid.create(page);
                });
    }
    
    @ByPermissionClass(AdminRoleMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(AdminRole adminRole){
        adminRoleServiceImpl.save(adminRole);
        return messageMv("保存成功！");
    }
    @ByPermissionClass(AdminRoleMgr.class)
    @RequestMapping(value="{id}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id){
        AdminRole adminRole = adminRoleServiceImpl.loadById(id);
        return responseData(adminRole);
    }
    
    @ByPermissionClass(AdminRoleMgr.Update.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, AdminRole adminRole){
        adminRole.setId(id);
        adminRoleServiceImpl.update(adminRole);
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(AdminRoleMgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ids){
        adminRoleServiceImpl.deleteByIds(ids);
        return messageMv("删除成功！");
    }
}