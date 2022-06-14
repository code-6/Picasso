package org.novinomad.picasso.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.gantt.Task;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourBindDTO implements DTO<Task>{
    Tour tour;
    List<TourBind> binds = new ArrayList<>();

    private static final String tourNameWithIcon = "<a href=\"#\"><i class=\"fa-solid fa-earth-asia\"></i> %s</a>";
    private static final String guideNameWithIcon = "<a href=\"#\"><i class=\"fa-solid fa-person-hiking\"></i> %s</a>";
    private static final String driverNameWithIcon = "<a href=\"#\"><i class=\"fa-solid fa-user-tie\"></i> %s</a>";

    @Override
    public Task dto() {

        Task ganttTourTask = new Task();
        ganttTourTask.setId(tour.getId());
        ganttTourTask.setName(String.format(tourNameWithIcon,tour.getName()));
        ganttTourTask.setStartDate(tour.getStartDate());
        ganttTourTask.setPlannedStartDate(tour.getStartDate());
        ganttTourTask.setEndDate(tour.getEndDate());
        ganttTourTask.setPlannedEndDate(tour.getEndDate());
        ganttTourTask.setBarText(tour.getName());
        ganttTourTask.setNotes(tour.getDescription());
        ganttTourTask.setCompletionPercent(tour.getCompletenessPercent());
        ganttTourTask.setType(Task.Type.PARENT);
        ganttTourTask.setCaption(tour.getName());

        if(tour.inPast())
            ganttTourTask.setCssClass(Task.CssClass.GREY.getCssName());
        else if(tour.inFuture())
            ganttTourTask.setCssClass(Task.CssClass.GREEN.getCssName());
        else
            ganttTourTask.setCssClass(Task.CssClass.BLUE.getCssName());

        binds.forEach(bind -> {
            Employee employee = bind.getEmployee();
            Employee.Type employeeType = employee.getType();
            String employeeName = employee.getName();
            String taskName = employeeType.equals(Employee.Type.GUIDE)
                    ? String.format(guideNameWithIcon, employeeName)
                    : String.format(driverNameWithIcon, employeeName);

            Task ganttEmployeeTask = new Task();
            ganttEmployeeTask.setId(bind.getId());
            ganttEmployeeTask.setName(taskName);
            ganttEmployeeTask.setStartDate(bind.getStartDate());
            ganttEmployeeTask.setPlannedStartDate(bind.getStartDate());
            ganttEmployeeTask.setEndDate(bind.getEndDate());
            ganttEmployeeTask.setPlannedEndDate(bind.getEndDate());
            ganttEmployeeTask.setParent(ganttTourTask);
            ganttEmployeeTask.setBarText(employeeName);
            ganttEmployeeTask.setNotes("tour id = " + tour.getId().toString() + " tour name = "+ tour.getName() + " parent task id = " + ganttTourTask.getId());
//            ganttEmployeeTask.setType(Task.Type.SINGLE);
            ganttTourTask.setCaption(employeeName);

            ganttTourTask.addChild(ganttEmployeeTask);

            switch (employeeType) {
                case GUIDE -> ganttEmployeeTask.setCssClass(Task.CssClass.YELLOW.getCssName());
                case DRIVER -> ganttEmployeeTask.setCssClass(Task.CssClass.PURPLE.getCssName());
                default -> ganttEmployeeTask.setCssClass(Task.CssClass.PINK.getCssName());
            }
        });
        return ganttTourTask;
    }
}
