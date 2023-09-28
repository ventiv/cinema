public class Movie {
    private int movieId;
    private String title;
    private String director;
    private String mainCast;
    private String synopsis;
    private int duration;
    private Category category;

    public Movie() {}

    public Movie(String title, String director, String mainCast, String synopsis, int duration, Category category) {
        this.title = title;
        this.director = director;
        this.mainCast = mainCast;
        this.synopsis = synopsis;
        this.duration = duration;
        this.category = category;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getMainCast() {
        return mainCast;
    }

    public void setMainCast(String mainCast) {
        this.mainCast = mainCast;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
