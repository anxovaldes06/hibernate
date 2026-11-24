package es.aula;

import java.util.Comparator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("sakila");
                EntityManager em = emf.createEntityManager()) {
            Object nombreBD = em
                    .createNativeQuery("SELECT DATABASE()")
                    .getSingleResult();
            System.out.println("Conexión correcta.");
            System.out.println("Base de datos: " + nombreBD);

            // clave primaria compuesta de film_actor formada por actor_id y film_id
            // Un actor puede actuar en muchas peliculas, pero no puede repetirse el mismo
            // actor y pelicula
            // relacion N:M , relacion muchos a muchos

            // ordena actores por actor_id

            List<Actor> actores = em
                    .createQuery("from Actor a order by a.actorId", Actor.class)
                    .getResultList();

            System.out.println("=== LISTADO DE ACTORES ===");
            for (Actor actor : actores) {
                System.out.printf("%d | %s | %s%n",
                        actor.getActorId(),
                        actor.getFirstName(),
                        actor.getLastName());
            }
            // devuelve número total de actores
            System.out.println("\n\n\nTotal de actores: " + actores.size() + "\n\n\n");
            System.out.println();

            // selecciona el actor_id , que debe ser 1 , de la tabla film_actor y hace un
            // join con tabla film,
            // sacando el film_id y titulo relacionado con ese actor

            Actor actorPrueba = em.find(Actor.class, 1);
            if (actorPrueba != null) {
                System.out.printf("=== PELÍCULAS DE %s %s (ID: %d) ===%n",
                        actorPrueba.getFirstName(),
                        actorPrueba.getLastName(),
                        actorPrueba.getActorId());

                List<String> titulosOrdenados = actorPrueba.getFilms().stream()
                        .map(Film::getTitle)
                        .sorted(Comparator.naturalOrder())
                        .toList();

                for (String titulo : titulosOrdenados) {
                    System.out.println("- " + titulo);
                }

                // número de películas en las que participo
                System.out.println("Total de películas del actor: " + titulosOrdenados.size());
            } else {
                System.out.println("No se encontró ningún actor con ID 1.");
            }
        }
    }
}