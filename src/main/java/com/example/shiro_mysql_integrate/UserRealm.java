package com.example.shiro_mysql_integrate;

import com.example.shiro_mysql_integrate.dao.UserDao;
import com.example.shiro_mysql_integrate.mapper.RoleMapper;
import com.example.shiro_mysql_integrate.model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("authorizer")
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = userDao.findUserByUsername(principalCollection.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(user.getRoleNames());
        info.setStringPermissions(roleMapper.getPermissionByRoleId(user.getRole().getId()));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userDao.findUserByUsername(token.getUsername());

        if (user == null) {
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
    }
}
