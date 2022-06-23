package org.novinomad.picasso.dto.bind;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomUtils;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.DTO;
import org.novinomad.picasso.dto.gantt.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindDTO implements DTO<Task> {
    Tour tour;
    List<TourBind> binds = new ArrayList<>();

    private static final String tourNameWithIcon = "<a href=\"http://localhost:8080/picasso/api/tour/%d\"><i class=\"fa-solid fa-earth-asia\"></i> %s</a>";
    private static final String guideNameWithIcon = "<a href=\"http://localhost:8080/picasso/api/employee/%d\"><i class=\"fa-solid fa-person-hiking\"></i> %s</a>";
    private static final String driverNameWithIcon = "<a href=\"http://localhost:8080/picasso/api/employee/%d\"><i class=\"fa-solid fa-user-tie\"></i> %s</a>";

    @Override
    public Task dto() {

        Long ganttTourTaskId = Long.parseLong("84" + tour.getId()); // 84 - ASCII symbol code (T)
        String ganttTourTaskName = String.format(tourNameWithIcon, tour.getId(), tour.getName());
        Task ganttTourTask = new Task(ganttTourTaskId, ganttTourTaskName, tour.getDateRange())
                .notes(tour.getDescription())
//                .completionPercent(tour.getCompletenessPercent())
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
}
