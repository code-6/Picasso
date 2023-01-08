package org.novinomad.picasso.domain.dto.tour.gantt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.ILocalDateTimeRange;
import org.novinomad.picasso.commons.serializers.ListOfEntitiesToCommaSeparatedString;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Tour bind DTO that converts data into JS gantt improved data model to draw gantt chart
 * */
@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task implements ITask, Comparable<Task> {
    @JsonProperty("pID")
    Long id;

    @JsonProperty("pParent")
    Long parentId = 0L;

    @JsonProperty("pName")
    String name;
    @JsonProperty("pStart")

    LocalDateTime startDate;
    @JsonProperty("pEnd")

    LocalDateTime endDate;
    @JsonProperty("pPlanStart")

    LocalDateTime plannedStartDate;
    @JsonProperty("pPlanEnd")

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

    @JsonProperty("tourId")
    Long tourId;

    public Task tourId(Long tourId) {
        this.tourId = tourId;
        return this;
    }

    @JsonProperty("participantIds")
    Set<Long> participantIds = new HashSet<>();

    public Task participantId(Long participantId) {
        participantIds.add(participantId);
        return this;
    }

    public Task participantId(TourParticipant tourParticipant) {
        participantIds.add(tourParticipant.getId());
        return this;
    }

    public Task participantIds(List<Long> tourParticipantIds) {
        this.participantIds.clear();
        this.participantIds.addAll(tourParticipantIds);
        return this;
    }

    public Task participantIds2(List<TourParticipant> tourParticipants) {
        this.participantIds.clear();
        this.participantIds.addAll(tourParticipants.stream().map(TourParticipant::getId).collect(Collectors.toSet()));
        return this;
    }

    @JsonProperty("bindIds")
    Set<Long> bindIds = new HashSet<>();

    public Task bindId(Long bindId) {
        bindIds.add(bindId);
        return this;
    }

    public Task bindId(TourBind tourBind) {
        bindIds.add(tourBind.getId());
        return this;
    }

    public Task bindIds(List<Long> bindIds) {
        this.bindIds.clear();
        this.bindIds.addAll(bindIds);
        return this;
    }

    public Task bindIds2(List<TourBind> bindIds) {
        this.bindIds.clear();
        this.bindIds.addAll(bindIds.stream().map(TourBind::getId).collect(Collectors.toSet()));
        return this;
    }

    @JsonIgnore
    Task parent;
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

    public Task(Long id, String name, ILocalDateTimeRange dateTimeRange) {
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
        return tasks;
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

        Task ganttTourTask = buildTourTask(tour).bindIds2(binds);

        binds.stream()
                .filter(tb -> tb.getTourParticipant() != null)
                .collect(Collectors.groupingBy(TourBind::getTourParticipant))
                .forEach((tourParticipant, tourParticipantBinds) -> {

                    ganttTourTask.participantId(tourParticipant);

                    if (!CollectionUtils.isEmpty(tourParticipantBinds) && tourParticipantBinds.get(0) != null) {

                        TourBind firstBind = tourParticipantBinds.get(0);

                        if (tourParticipantBinds.size() > 1) {

                            long combinedTaskId = Long.parseLong("838466" + firstBind.getId());

                            Task tourParticipantCombinedTask = buildTourParticipantTask(ganttTourTask, tourParticipant, combinedTaskId, tour.getDateTimeRange(), Type.COMBINED)
                                    .bindIds2(tourParticipantBinds)
                                    .tourId(tour.getId())
                                    .participantId(tourParticipant);

                            Task tourParticipantTask = null;

                            for (TourBind bind : tourParticipantBinds) {

                                long tourParticipantTaskId = Long.parseLong("66" + bind.getId());

                                tourParticipantTask = buildTourParticipantTask(tourParticipantCombinedTask, tourParticipant, tourParticipantTaskId, bind.getDateTimeRange(), Type.SINGLE, tourParticipantTask)
                                        .bindId(bind)
                                        .tourId(tour.getId())
                                        .participantId(tourParticipant.getId());
                            }
                        } else {
                            long tourParticipantTaskId = Long.parseLong("66" + firstBind.getId());

                            buildTourParticipantTask(ganttTourTask, tourParticipant, tourParticipantTaskId, firstBind.getDateTimeRange(), Type.SINGLE)
                                    .bindIds2(tourParticipantBinds)
                                    .tourId(tour.getId())
                                    .participantId(tourParticipant.getId());
                        }
                    }
                });
        return ganttTourTask;
    }

    private static Task buildTourTask(Tour tour) {
        Long ganttTourTaskId = Long.parseLong("84" + tour.getId()); // 84 - ASCII symbol code (T)

        Task ganttTourTask = new Task(ganttTourTaskId, tour.getId() + ". " + tour.getName(), tour.getDateTimeRange())
                .notes(tour.getDescription())
                .completionPercent(tour.getDateTimeRange().getCompletenessPercent())
                .type(Type.PARENT)
                .tourId(tour.getId());

        if (tour.getDateTimeRange().inPast())
            ganttTourTask.cssClass(Task.CssClass.GREY.getCssName());
        else if (tour.getDateTimeRange().inFuture())
            ganttTourTask.cssClass(Task.CssClass.GREEN.getCssName());
        else
            ganttTourTask.cssClass(Task.CssClass.BLUE.getCssName());

        return ganttTourTask;
    }

    private static Task buildTourParticipantTask(Task parentTask, TourParticipant tourParticipant, Long taskId, ILocalDateTimeRange taskDates, Task.Type type, Task... dependencies) {

        Long tourParticipantId = tourParticipant.getId();
        TourParticipant.Type tourParticipantType = tourParticipant.getType();
        String tourParticipantName = tourParticipant.getName();

        Task ganttTourParticipantTask = new Task(taskId, tourParticipantType + " " + tourParticipantId + ". " + tourParticipantName, taskDates)
                .parent(parentTask).type(type)
                .participantId(tourParticipantId);

        if (dependencies != null && dependencies.length > 0) {
            ArrayList<Task> dependencyTasks = new ArrayList<>();
            for (Task dependency : dependencies) {
                if (dependency != null)
                    dependencyTasks.add(dependency);
            }
            ganttTourParticipantTask.dependencies(dependencyTasks);
        }

        ganttTourParticipantTask.cssClass(tourParticipantType.getGanttTaskColor().getCssName());

        parentTask.addChild(ganttTourParticipantTask);

        return ganttTourParticipantTask;
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
        if (startDate.equals(o.getStartDate()))
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
