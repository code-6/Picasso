//package org.novinomad.picasso.erm.entities.system;
//
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.OneToMany;
//import java.util.HashSet;
//import java.util.Set;
//
//public class HttpRouteAccessConfig {
//
//    private String urlPattern;
//
//    @OneToMany
//    @JoinTable(name = "gui_element_permissions",
//            joinColumns = @JoinColumn(name = "gui_element_id"),
//            inverseJoinColumns = @JoinColumn(name = "permission_name"))
//    private Set<Permission> permissions = new HashSet<>();
//}
