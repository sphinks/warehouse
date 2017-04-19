package com.not4j

import com.not4j.warehouse.auth.Role
import com.not4j.warehouse.auth.User
import com.not4j.warehouse.auth.UserRole
import com.not4j.warehouse.entity.Item

class BootStrap {

    def init = { servletContext ->
        new Item(externalId: "someId1", name: "Savage", brand: "Dior", price: 3037.98, size: 100.0, amount: 15).save()
        new Item(externalId: "someId2", name: "Sport", brand: "Dior", price: 2037.98, size: 50.0, amount: 3).save()
        new Item(externalId: "someId3", name: "Colongue", brand: "Dior", price: 4037.98, size: 100.0, amount: 6).save()

        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def userRole = new Role(authority: 'ROLE_USER').save()

        def adminUser = new User(username: 'admin', password: 'admin').save()
        def testUser = new User(username: 'test', password: 'test').save()

        UserRole.create adminUser, adminRole
        UserRole.create testUser, userRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }

        assert User.count() == 2
        assert Role.count() == 2
        assert UserRole.count() == 2
    }
    def destroy = {
    }
}
