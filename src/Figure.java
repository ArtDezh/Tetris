public class Figure {
    public void rotate() {
    }

    public void drop() {
    }

    public void move(int direction) {
    }

    // проверяет, касается ли фигура нижней части игравого поля или другой фигуры
    public boolean isTouchGround() {
        return false;
    }

    // оставляет фигуру на месте, куда она упала
    public void leaveOnTheGround() {
    }
    // прверяет, на переполнение нашего игравого поля
    public boolean isCrossGround() {
        return false;
    }

    // опускает фигуру на один уровень
    public void stepDown() {
    }
}
