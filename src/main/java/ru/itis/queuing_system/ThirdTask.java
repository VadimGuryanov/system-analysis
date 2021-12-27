package ru.itis.queuing_system;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ThirdTask extends AbstractTask {

    @Override
    public void avgAndDispersion(List<Timestamp> dates) {
        Map<LocalDate, Long> applications = new HashMap<>();

        Arrays.asList(DayOfWeek.values()).forEach(
                day -> {
                    var jule = dates.stream().map(Timestamp::toLocalDateTime)
                            .filter(t -> t.getYear() == 2020)
                            .filter(t -> t.getMonth() == Month.JULY)
                            .filter(t -> t.getDayOfWeek() == day)
                            .collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.counting()));
                    var august = dates.stream().map(Timestamp::toLocalDateTime)
                            .filter(t -> t.getYear() == 2020)
                            .filter(t -> t.getMonth() == Month.AUGUST)
                            .filter(t -> t.getDayOfWeek() == day)
                            .collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.counting()));
                    var september = dates.stream().map(Timestamp::toLocalDateTime)
                            .filter(t -> t.getYear() == 2020)
                            .filter(t -> t.getMonth() == Month.SEPTEMBER)
                            .filter(t -> t.getDayOfWeek() == day)
                            .collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.counting()));

                    applications.putAll(jule);
                    applications.putAll(august);
                    applications.putAll(september);

                    int i = 1;
                    for (var d : applications.values()) {
                        log.info("{} №{}; Кол-во заявок = {}\n", day.getDisplayName(TextStyle.FULL, Locale.ROOT), i, d);
                        i++;
                    }

                    getDispersion(getAverage(applications.values()), applications.values());
                }
        );
    }

    @Override
    protected void getDispersion(double avg, Collection<Long> applicationAmountInDay) {
        var sum = applicationAmountInDay.stream().map(
                        l -> Math.pow((l - avg), 2)
                )
                .reduce(0.0, Double::sum);
        var dispersion = Math.sqrt(sum / (applicationAmountInDay.size()));

        log.info("Средне квадратичная ошибка = {} (кол. заявок/час)\n", dispersion / 24);
    }

    @Override
    protected double getAverage(Collection<Long> applicationAmountInDay) {
        var avg = (double) applicationAmountInDay.stream()
                .reduce(0L, Long::sum) / applicationAmountInDay.size();
        log.info("Среднее значение = {} (кол. заявок/час)\n", avg / 24);
        return avg;
    }
}
