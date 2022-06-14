package org.novinomad.picasso.dto.gantt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.serializers.ListToCommaSeparatedString;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class Task implements ITask {

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
    String resourceName;

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

    @JsonProperty("pCost")
    Long cost = 0L;

    @JsonProperty("pNotes")
    String notes;

    @JsonProperty("pBarText")
    String barText;

    @JsonIgnore
    Task parent;

    @JsonProperty("pParent")
    Long parentId = 0L;

    @JsonIgnore
    List<Task> children = new ArrayList<>();

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
}
