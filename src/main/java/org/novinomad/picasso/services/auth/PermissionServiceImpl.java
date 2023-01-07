package org.novinomad.picasso.services.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.LogIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.enums.CRUD;
import org.novinomad.picasso.commons.exceptions.CRUDException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.PermissionUtil;
import org.novinomad.picasso.domain.dto.permissions.VisJsDataSet;
import org.novinomad.picasso.domain.erm.entities.auth.IPermission;
import org.novinomad.picasso.domain.erm.entities.auth.Permission;
import org.novinomad.picasso.repositories.jpa.PermissionRepository;
import org.novinomad.picasso.services.AbstractCrudCacheService;
import org.slf4j.event.Level;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@DependsOn("springContextUtil")
public class PermissionServiceImpl extends AbstractCrudCacheService<Long, Permission> implements PermissionService {

    private final PermissionRepository permissionRepository;

    protected PermissionServiceImpl(PermissionRepository permissionRepository) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    void buildBasePermissionGraph() {

        Permission rootPermission = permissionRepository.findByName(ROOT_PERMISSION_NAME)
                .orElseGet(() -> save(new Permission(ROOT_PERMISSION_NAME, "allowed to do everything in the system")));

        if(rootPermission.getChildren().isEmpty()) {

            // CREATE
            IPermission permissionCreate = new Permission("create", "allowed to create anything in the system");

            IPermission permissionCreateTours = new Permission("create tours", "allowed to create tours");
            IPermission permissionCreateTourParticipants = new Permission("create tour participants", "allowed to create tour participants of any type");
            IPermission permissionCreateTourBinds = new Permission("create tour binds", "allowed to bind participants to the tours (on main page of the system)");
            IPermission permissionCreateUsers = new Permission("create users", "allowed to create users");
            IPermission permissionCreatePermissions = new Permission("create permissions", "allowed to create permissions");

            permissionCreate.addChild(permissionCreateTours);
            permissionCreate.addChild(permissionCreateTourParticipants);
            permissionCreate.addChild(permissionCreateTourBinds);
            permissionCreate.addChild(permissionCreateUsers);
            permissionCreate.addChild(permissionCreatePermissions);

            rootPermission.addChild(permissionCreate);

            // READ
            IPermission permissionRead = new Permission("read", "allowed to see anything in the system");

            IPermission permissionReadTours = new Permission("read tours", "allowed to see list of tours");
            IPermission permissionReadTourParticipants = new Permission("read tour participants", "allowed to see list of tour participants of any type");
            IPermission permissionReadTourBinds = new Permission("read tour binds", "allowed to see list of tour binds (on main page of the system)");
            IPermission permissionReadUsers = new Permission("read users", "allowed to see list of users");
            IPermission permissionReadPermission = new Permission("read permissions", "allowed to see permissions graph");

            permissionRead.addChild(permissionReadTours);
            permissionRead.addChild(permissionReadTourParticipants);
            permissionRead.addChild(permissionReadTourBinds);
            permissionRead.addChild(permissionReadUsers);
            permissionRead.addChild(permissionReadPermission);

            rootPermission.addChild(permissionRead);

            // UPDATE
            IPermission permissionUpdate = new Permission("update", "allowed to update anything in the system");

            IPermission permissionUpdateTours = new Permission("update tours", "allowed to update tours");
            IPermission permissionUpdateTourParticipants = new Permission("update tour participants", "allowed to update tour participants of any type");
            IPermission permissionUpdateTourBinds = new Permission("update tour binds", "allowed to update tour binds (on main page of the system)");
            IPermission permissionUpdateUsers = new Permission("update users", "allowed to update users");
            IPermission permissionUpdatePermissions = new Permission("update permissions", "allowed to update permissions");

            permissionUpdate.addChild(permissionUpdateTours);
            permissionUpdate.addChild(permissionUpdateTourParticipants);
            permissionUpdate.addChild(permissionUpdateTourBinds);
            permissionUpdate.addChild(permissionUpdateUsers);
            permissionUpdate.addChild(permissionUpdatePermissions);

            rootPermission.addChild(permissionUpdate);

            // DELETE
            IPermission permissionDelete = new Permission("delete", "allowed to delete anything in the system");

            IPermission permissionDeleteTours = new Permission("delete tours", "allowed to delete tours");
            IPermission permissionDeleteTourParticipants = new Permission("delete tour participants", "allowed to delete tour participants of any type");
            IPermission permissionDeleteTourBinds = new Permission("delete tour binds", "allowed to delete tour binds (on main page of the system)");
            IPermission permissionDeleteUsers = new Permission("delete users", "allowed to delete users");
            IPermission permissionDeletePermissions = new Permission("delete permissions", "allowed to delete permissions");

            permissionDelete.addChild(permissionDeleteTours);
            permissionDelete.addChild(permissionDeleteTourParticipants);
            permissionDelete.addChild(permissionDeleteTourBinds);
            permissionDelete.addChild(permissionDeleteUsers);
            permissionDelete.addChild(permissionDeletePermissions);

            rootPermission.addChild(permissionDelete);

            Permission savedPermissionGraph = permissionRepository.save(rootPermission);

            log.info("base permissions graph initialized {}", savedPermissionGraph);

//            Permission basePermissionGraph = (Permission) rootPermission
//                    .addChild(
//                            new Permission("read", "allowed to see anything in the system")
//                                    .addChild("read tours", "allowed to see list of tours")
//                                    .addChild("read tour participants", "allowed to see list of tour participants of any type")
//                                    .addChild("read tour binds", "allowed to see list of tour binds (on main page of the system)")
//                                    .addChild("read users", "allowed to see list of users")
//                                    .addChild("read permissions", "allowed to see permissions graph")
//                    )
//                    .addChild(
//                            new Permission("create", "allowed to create anything in the system")
//                                    .addChild("create tours", "allowed to create tours")
//                                    .addChild("create tour participants", "allowed to create tour participants of any type")
//                                    .addChild("create tour binds", "allowed to bind participants to the tours (on main page of the system)")
//                                    .addChild("create users", "allowed to create users")
//                                    .addChild("create permissions", "allowed to create permissions")
//                    )
//                    .addChild(
//                            new Permission("update", "allowed to update anything in the system")
//                                    .addChild("update tours", "allowed to update tours")
//                                    .addChild("update tour participants", "allowed to update tour participants of any type")
//                                    .addChild("update tour binds", "allowed to update tour binds (on main page of the system)")
//                                    .addChild("update users", "allowed to update users")
//                                    .addChild("update permissions", "allowed to update permissions")
//                    )
//                    .addChild(
//                            new Permission("delete", "allowed to delete anything in the system")
//                                    .addChild("delete tours", "allowed to delete tours")
//                                    .addChild("delete tour participants", "allowed to delete tour participants of any type")
//                                    .addChild("delete tour binds", "allowed to delete tour binds (on main page of the system)")
//                                    .addChild("delete users", "allowed to delete users")
//                                    .addChild("delete permissions", "allowed to delete permissions")
//                    );
        }

        log.info("ROOT permission initialized {}", rootPermission);
    }

    @Override
    @Transactional
    public Optional<Permission> get(String permissionName) {
        return CACHE.asMap().values().parallelStream()
                .filter(p -> permissionName.equalsIgnoreCase(p.getName())).findAny();
    }


    @Override
    public VisJsDataSet getAllForVisJs() {

        return PermissionUtil.toVisJsDataSet(permissionRepository.getRootPermissions());
    }
}
