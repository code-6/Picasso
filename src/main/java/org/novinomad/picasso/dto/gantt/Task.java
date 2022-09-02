package org.novinomad.picasso.dto.gantt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.serializers.ListOfEntitiesToCommaSeparatedString;
import org.novinomad.picasso.commons.serializers.ListToCommaSeparatedString;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.novinomad.picasso.commons.utils.CommonMessageFormat.*;


@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task implements ITask, Comparable<Task> {
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
    @JsonSerialize(using = ListOfEntitiesToCommaSeparatedString.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return milestone == task.milestone && expanded == task.expanded && id.equals(task.id) && name.equals(task.name) && startDate.equals(task.startDate) && endDate.equals(task.endDate) && Objects.equals(plannedStartDate, task.plannedStartDate) && Objects.equals(plannedEndDate, task.plannedEndDate) && Objects.equals(cssClass, task.cssClass) && Objects.equals(webLink, task.webLink) && Objects.equals(resourceName, task.resourceName) && Objects.equals(completionPercent, task.completionPercent) && type == task.type && Objects.equals(caption, task.caption) && Objects.equals(notes, task.notes) && Objects.equals(cost, task.cost) && Objects.equals(barText, task.barText) && Objects.equals(parentId, task.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, endDate, plannedStartDate, plannedEndDate, type, parentId);
    }

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
        List<Task> tasks = new ArrayList<>();
        deTree(fromBinds(binds), tasks);
        return new ArrayList<>(tasks);
    }

    private static void deTree(Collection<Task> tasks, List<Task> container) {
        for (Task task : tasks)
            if (task != null) {
                container.add(task);
                if (task.hasChildren()) deTree(task.getChildren(), container);
                if (task.hasDependencies()) deTree(task.getDependencies(), container);
            }
    }

    private static final String ANCHOR = """
            <a id="taskId_${id}" class="${entityType}-task" data-type="${entityType}" data-${entityType}-id="${entityId}">${taskName}</a>
            """;

    private static Task build(Tour tour, List<TourBind> binds) {
        Task ganttTourTask = buildTourTask(tour);

        binds.stream().collect(Collectors.groupingBy(TourBind::getEmployee)).forEach((employee, employeeBinds) -> {

            if(!CollectionUtils.isEmpty(employeeBinds) && employeeBinds.get(0) != null) {
                TourBind firstBind = employeeBinds.get(0);
                if(employeeBinds.size() > 1) {
                    Task employeeCombinedTask = buildEmployeeTask(ganttTourTask, employee, Long.parseLong("838466" + firstBind.getId()), tour.getDateRange(), Type.COMBINED);
                    Task employeeTask = null;
                    for (TourBind bind : employeeBinds) {
                        employeeTask = buildEmployeeTask(employeeCombinedTask, employee, Long.parseLong("66" + bind.getId()), bind.getDateRange(), Type.SINGLE, employeeTask);
                    }
                } else {
                    buildEmployeeTask(ganttTourTask, employee, Long.parseLong("66" + firstBind.getId()), firstBind.getDateRange(), Type.SINGLE);
                }
            }
        });
        return ganttTourTask;
    }

    private static Task buildTourTask(Tour tour) {
        Long ganttTourTaskId = Long.parseLong("84" + tour.getId()); // 84 - ASCII symbol code (T)

        Task ganttTourTask = new Task(ganttTourTaskId,tour.getId() + ". " + tour.getName(), tour.getDateRange())
                .notes(tour.getDescription())
                .completionPercent(tour.getCompletenessPercent())
                .type(Task.Type.PARENT);

        if (tour.inPast())
            ganttTourTask.cssClass(Task.CssClass.GREY.getCssName());
        else if (tour.inFuture())
            ganttTourTask.cssClass(Task.CssClass.GREEN.getCssName());
        else
            ganttTourTask.cssClass(Task.CssClass.BLUE.getCssName());

        return ganttTourTask;
    }

    private static Task buildEmployeeTask(Task parentTask, Employee employee, Long taskId, LocalDateTimeRange taskDates, Task.Type type, Task ... dependencies) {

        Long employeeId = employee.getId();
        Employee.Type employeeType = employee.getType();
        String employeeName = employee.getName();

        Task ganttEmployeeTask = new Task(taskId, employeeType + " " + employeeId + ". " + employeeName, taskDates)
                .parent(parentTask).type(type);

        if(dependencies != null && dependencies.length > 0) {
            ArrayList<Task> dependencyTasks = new ArrayList<>();
            for (Task dependency : dependencies) {
                if(dependency != null)
                    dependencyTasks.add(dependency);
            }
            ganttEmployeeTask.dependencies(dependencyTasks);
        }

        ganttEmployeeTask.cssClass(employeeType.getCOLOR().getCssName());

        parentTask.addChild(ganttEmployeeTask);

        return ganttEmployeeTask;
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

    @Override
    public int compareTo(Task o) {
        if(startDate.equals(o.getStartDate()))
            return endDate.compareTo(o.getEndDate());

        return startDate.compareTo(o.getStartDate());
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
