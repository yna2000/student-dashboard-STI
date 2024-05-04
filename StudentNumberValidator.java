class StudentNumberValidator<T> {
    private T studentNumber;
    public void setStudentNumber(T num) {
        studentNumber = num;
    }
    public T getStudentNumber() {
        return studentNumber;
    }
    public boolean isValidStudentNumber() {
        String regex = "\\d{4}-\\d{2}-\\d{3}";
        return studentNumber.toString().matches(regex);
    }
}
