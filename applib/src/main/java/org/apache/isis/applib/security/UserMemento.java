/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.applib.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NotPersistable;

/**
 * Details, obtained from the container, about the user and his roles. Read-only.
 */
@NotPersistable
public final class UserMemento {

    /**
     * Creates a new user with the specified name and no roles.
     */
    public UserMemento(final String name) {
    	this(name, new RoleMemento[0]);
    }

    /**
     * Creates a new user with the specified name and assigned roles.
     */
    public UserMemento(final String name, final RoleMemento... roles) {
    	this(name, Arrays.asList(roles));
    }

    /**
     * Creates a new user with the specified name and assigned roles.
     */
    public UserMemento(final String name, final List<RoleMemento> roles) {
        if (name == null) {
            throw new IllegalArgumentException("Name not specified");
        }
        this.name = name;
        this.roles.addAll(roles);
    }


    // {{ Identification: Title
    public String title() {
        return name;
    }
    // }}


    // {{ (User) Name, isCurrentUser
    private final String name;
    /**
     * The user's login name.
     */
    @MemberOrder(sequence="1.1")
    public String getName() {
        return name;
    }
    
    
    /**
     * Determine if the specified user is this user. Returns true if the names match (is case sensitive).
     */
    public boolean isCurrentUser(final String user) {
        if (user == null) {
            throw new IllegalStateException("no user specified");
        }
        return name.equals(user);
    }
    // }}


    
    // {{ Roles
    private final List<RoleMemento> roles = new ArrayList<RoleMemento>();

    /**
     * The roles associated with this user.
     */
    @MemberOrder(sequence="1.1")
    public List<RoleMemento> getRoles() {
        return roles;
    }

    /**
     * Determines if the user fulfills the specified role.
     */
    public boolean hasRole(final RoleMemento role) {
        return hasRole(role.getName());
    }

    /**
     * Determines if the user fulfills the specified role. Roles are compared lexically by role name.
     */
    public boolean hasRole(final String roleName) {
    	for(RoleMemento role: roles) {
    		if (role.getName().equals(roleName)) {
    			return true;
    		}
    	}
    	return false;
    }
    // }}

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (RoleMemento role: roles) {
            buf.append(role.getName()).append(" ");
        }
        return "User [name=" + getName() + ",roles=" + buf.toString() + "]";
    }

}
