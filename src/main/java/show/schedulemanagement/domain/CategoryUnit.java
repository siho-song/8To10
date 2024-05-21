package show.schedulemanagement.domain;

public enum CategoryUnit {
    PAGE("페이지"),CHAPTER("챕터"),LECTURE("강의"),PROJECT("프로젝트"),WORKOUT("운동"),NONE("해당 없음");

    private final String unit;

    CategoryUnit(String unit) {
        this.unit = unit;
    }
}
