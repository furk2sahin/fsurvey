package project.fsurvey.business.abstracts;

public interface CrudService<T> {
    T add(T t);
    T update(Long id, T t);
    void delete(Long id);
}
