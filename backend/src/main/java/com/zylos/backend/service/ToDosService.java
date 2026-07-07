package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.ToDos;
import com.zylos.backend.repository.ToDosRepository;

import java.util.Comparator;
import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class ToDosService {

    @Autowired
    ToDosRepository toDosRepository;

    //sortierung vlt noch ändern
    public List<ToDos> listeAllerTodosEinerProjektgruppe(int projektgruppenId) {
        List<ToDos> toDosList = toDosRepository.findAllByProjektgruppenId(projektgruppenId);
        toDosList.sort(new Comparator<ToDos>() {
            @Override
            public int compare(ToDos o1, ToDos o2) {
                if (o1.getId() < o2.getId()) {return -1;}
                else if (o1.getId() > o2.getId()) {return 1;}
                else {return 0;}
            }
        });
        return toDosList;
    }

    public boolean fuegeTodoHinzu(int projektgruppe, String inhalt, int verantwortlicherId) {
        toDosRepository.save(new ToDos(projektgruppe, inhalt, verantwortlicherId, false));
        return true;
    }

    public boolean TodoErledigt(int todoId) {
        ToDos todos = toDosRepository.findById(todoId);
        todos.setErledigt(true);
        toDosRepository.save(todos);
        return true;
    }

    public boolean aenderVerantwortlichen(int todoId, int verantwortlichenId) {
        ToDos todos = toDosRepository.findById(todoId);
        todos.setVerantwortlichenId(verantwortlichenId);
        toDosRepository.save(todos);
        return true;
    }
}
