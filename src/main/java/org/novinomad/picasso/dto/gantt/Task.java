package org.novinomad.picasso.dto.gantt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.serializers.ListToCommaSeparatedString;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@EqualsAndHashCode
@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task implements ITask {

    @JsonIgnore
    private static final String tourNameWithIcon = "<a href=\"http://localhost:8080/picasso/api/tour/%d\"><i class=\"fa-solid fa-earth-asia\"></i> %s</a>";
    @JsonIgnore
    private static final String guideNameWithIcon = "<a href=\"http://localhost:8080/picasso/api/employee/%d\"><i class=\"fa-solid fa-person-hiking\"></i> %s</a>";
    @JsonIgnore
    private static final String driverNameWithIcon = "<a href=\"http://localhost:8080/picasso/api/employee/%d\"><i class=\"fa-solid fa-user-tie\"></i> %s</a>";

    @JsonProperty("pID")
    Long id;
    @JsonProperty("pName")
    String name;
    @JsonProperty("pStart")
    @DateTimeFormat(pattern = CommonDateUtils.ISO_8601_WITHOUT_SECONDS)
    LocalDateTime startDate;
    @JsonProperty("pEnd")
    @DateTimeFormat(pattern = CommonDateUtils.ISO_8601_WITHOUT_SECONDS)
    LocalDateTime endDate;
    @JsonProperty("pPlanStart")
    @DateTimeFormat(pattern = CommonDateUtils.ISO_8601_WITHOUT_SECONDS)
    LocalDateTime plannedStartDate;
    @JsonProperty("pPlanEnd")
    @DateTimeFormat(pattern = CommonDateUtils.ISO_8601_WITHOUT_SECONDS)
    LocalDateTime plannedEndDate;
    @JsonProperty("pClass")
    String cssClass = CssClass.BLUE.cssName;
    @JsonProperty("pLink")
    String webLink;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("pMile")
    boolean milestone = false;
    @JsonProperty("pRes")
    String resourceName = "";
    @JsonProperty("pComp")
    Double completionPercent = 0d;
    @JsonProperty("pGroup")
    Type type = Type.SINGLE;
    @JsonProperty("pOpen")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    boolean expanded = false;
    @JsonProperty("pDepend")
    @JsonSerialize(using = ListToCommaSeparatedString.class)
    List<Task> dependencies = new ArrayList<>();
    @JsonProperty("pCaption")
    String caption;
    @JsonProperty("pNotes")
    String notes;
    @JsonProperty("pCost")
    Long cost = 0L;
    @JsonProperty("pBarText")
    String barText;
    @JsonIgnore
    Task parent;
    @JsonProperty("pParent")
    Long parentId = 0L;
    @JsonIgnore
    List<Task> children = new ArrayList<>();

    public Task(Long id, String name, LocalDateTimeRange dateTimeRange) {
        this.id = id;
        this.name = name;
        this.caption = name;
        this.barText = name;
        this.startDate = dateTimeRange.getStartDate();
        this.plannedStartDate = dateTimeRange.getStartDate();
        this.endDate = dateTimeRange.getEndDate();
        this.plannedEndDate = dateTimeRange.getEndDate();
    }

    private static List<Task> build(Map<Tour, List<TourBind>> map) {
        return map.entrySet().stream().map(e -> build(e.getKey(), e.getValue())).toList();
    }

    public static List<Task> fromBinds(List<TourBind> binds) {
        return build(binds.stream().collect(Collectors.groupingBy(TourBind::getTour)));
    }

    public static List<Task> fromBindsWithChildrenInList(List<TourBind> binds) {
        List<Task> tasks = fromBinds(binds);

        List<Task> allTasks = new ArrayList<>();

        tasks.forEach(gd -> {
            allTasks.add(gd);
            allTasks.addAll(gd.getChildren());
        });
        return allTasks;
    }

    private static Task build(Tour tour, List<TourBind> binds) {

        Long ganttTourTaskId = Long.parseLong("84" + tour.getId()); // 84 - ASCII symbol code (T)
        String ganttTourTaskName = String.format(tourNameWithIcon, tour.getId(), tour.getName());
        Task ganttTourTask = new Task(ganttTourTaskId, ganttTourTaskName, tour.getDateRange())
                .notes(tour.getDescription())
                .completionPercent(tour.getCompletenessPercent())
                .type(Task.Type.PARENT);

        if(tour.inPast())
            ganttTourTask.cssClass(Task.CssClass.GREY.getCssName());
        else if(tour.inFuture())
            ganttTourTask.cssClass(Task.CssClass.GREEN.getCssName());
        else
            ganttTourTask.cssClass(Task.CssClass.BLUE.getCssName());

        binds.forEach(bind -> {
            long ganttEmployeeTaskId = Long.parseLong("66" + bind.getId());
            Employee employee = bind.getEmployee();
            Long employeeId = employee.getId();
            Employee.Type employeeType = employee.getType();
            String employeeName = employee.getName();
            String ganttEmployeeTaskName= employeeType.equals(Employee.Type.GUIDE)
                    ? String.format(guideNameWithIcon, employeeId, employeeName)
                    : String.format(driverNameWithIcon, employeeId, employeeName);

            // 66 - ASCII symbol code (B)
            Task ganttEmployeeTask = new Task(ganttEmployeeTaskId, ganttEmployeeTaskName, bind.getDateRange())
                    .parent(ganttTourTask);

            ganttTourTask.addChild(ganttEmployeeTask);

            switch (employeeType) {
                case GUIDE -> ganttEmployeeTask.cssClass(Task.CssClass.YELLOW.getCssName());
                case DRIVER -> ganttEmployeeTask.cssClass(Task.CssClass.PURPLE.getCssName());
                default -> ganttEmployeeTask.cssClass(Task.CssClass.PINK.getCssName());
            }
        });
        return ganttTourTask;
    }

    public Task id(Long id) {
        this.id = id;
        return this;
    }

    public Task name(String name) {
        this.name = name;
        return this;
    }

    public Task startDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public Task endDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public Task plannedStartDate(LocalDateTime plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
        return this;
    }

    public Task plannedEndDate(LocalDateTime plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
        return this;
    }

    public Task cssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    public Task webLink(String webLink) {
        this.webLink = webLink;
        return this;
    }

    public Task milestone(boolean milestone) {
        this.milestone = milestone;
        return this;
    }

    public Task resourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public Task completionPercent(Double completionPercent) {
        this.completionPercent = completionPercent;
        return this;
    }

    public Task type(Type type) {
        this.type = type;
        return this;
    }

    public Task expanded(boolean expanded) {
        this.expanded = expanded;
        return this;
    }

    public Task dependencies(List<Task> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    public Task caption(String caption) {
        this.caption = caption;
        return this;
    }

    public Task cost(Long cost) {
        this.cost = cost;
        return this;
    }

    public Task notes(String notes) {
        this.notes = notes;
        return this;
    }

    public Task barText(String barText) {
        this.barText = barText;
        return this;
    }

    public Task parent(Task parent) {
        this.parent = parent;
        parentId = parent.getId();
        return this;
    }

    public Task children(List<Task> children) {
        this.children = children;
        return this;
    }

    public Task child(Task child) {
        children.add(child);
        return this;
    }

    public void setParent(Task parent) {
        this.parent = parent;
        parentId = parent.getId();
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public enum Type {
        SINGLE,
        PARENT,
        COMBINED;

        @JsonValue
        public int jsonValue() {
            return ordinal();
        }
    }

    @RequiredArgsConstructor
    public enum CssClass {
        YELLOW("gtaskyellow"),
        BLUE("gtaskblue"),
        GREY("gtaskgrey"),
        RED("gtaskred"),
        PURPLE("gtaskpurple"),
        GREEN("gtaskgreen"),
        PINK("gtaskpink");

        @Getter
        final String cssName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", children=" + children.stream().map(Task::getId).toList() +
                '}';
    }
}
