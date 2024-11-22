package com.example.edu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.example.edu.model.Schedule;
import com.example.edu.repository.ScheduleRepository;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Map<String, Map<String, List<Map<String, Object>>>> getScheduleForWeek(LocalDate startOfWeek) {
        logger.info("Initialisation de l'emploi du temps pour la semaine du {} au {}", startOfWeek, startOfWeek.plusDays(6));

        Map<String, Map<String, List<Map<String, Object>>>> schedule = new LinkedHashMap<>();
        List<String> days = List.of("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi");
        List<String> timeSlots = List.of("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");

        // Initialiser l'emploi du temps vide
        for (String day : days) {
            Map<String, List<Map<String, Object>>> dailySchedule = new LinkedHashMap<>();
            for (String time : timeSlots) {
                dailySchedule.put(time, new ArrayList<>());
            }
            schedule.put(day, dailySchedule);
        }

        // Définir les plages horaires de la semaine
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = startDateTime.plusDays(6).withHour(23).withMinute(59).withSecond(59);

        logger.info("Chargement des données de la base de données entre {} et {}", startDateTime, endDateTime);

        // Charger les données réelles depuis la base de données
        List<Schedule> schedules;
        try {
            schedules = scheduleRepository.findByStartBetween(startDateTime, endDateTime);
            logger.info("{} événements trouvés dans la base de données", schedules.size());
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des données depuis la base de données : {}", e.getMessage(), e);
            return schedule; // Retourne un emploi du temps vide en cas d'erreur
        }

        for (Schedule s : schedules) {
            String day;
            if (s.getStart().getDayOfWeek().getValue() == 7) {
                continue; // Ignorer le dimanche
            } else {
                day = days.get(s.getStart().getDayOfWeek().getValue() - 1);
            }

            // Détails du cours
            Map<String, Object> courseDetails = new HashMap<>();
            courseDetails.put("title", s.getCourse().getName());
            courseDetails.put("location", s.getRoom().getName());
            courseDetails.put("startTime", s.getStart().toLocalTime());
            courseDetails.put("endTime", s.getEnd().toLocalTime());
            courseDetails.put("teacherSurname", s.getTeacher().getSurname());

            // Calculer les créneaux impactés
            List<String> affectedSlots = calculateTimeSlots(s.getStart(), s.getEnd(), timeSlots);

            for (String slot : affectedSlots) {
                schedule.get(day).get(slot).add(courseDetails);
            }
        }

        logger.info("Emploi du temps généré avec succès");
        return schedule;
    }

    /**
     * Calcule les créneaux horaires affectés par un cours.
     * @param start Heure de début du cours.
     * @param end Heure de fin du cours.
     * @param timeSlots Liste des créneaux horaires (ex: "08:00", "09:00").
     * @return Liste des créneaux affectés.
     */
    private List<String> calculateTimeSlots(LocalDateTime start, LocalDateTime end, List<String> timeSlots) {
        List<String> affectedSlots = new ArrayList<>();
        for (String slot : timeSlots) {
            // Convertir le créneau en LocalDateTime
            LocalDateTime slotStart = start.toLocalDate().atTime(Integer.parseInt(slot.split(":")[0]), Integer.parseInt(slot.split(":")[1]));
            LocalDateTime slotEnd = slotStart.plusHours(1);

            // Vérifier si le cours chevauche ce créneau
            if ((start.isBefore(slotEnd) && end.isAfter(slotStart)) || start.equals(slotStart)) {
                affectedSlots.add(slot);
            }
        }
        return affectedSlots;
    }
}
