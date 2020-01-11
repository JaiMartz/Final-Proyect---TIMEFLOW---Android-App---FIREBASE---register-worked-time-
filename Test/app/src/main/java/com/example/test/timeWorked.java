package com.example.test;

public class timeWorked {
    private String clave;
    private String horaEntrada;
    private String horaSalida;
    private String dia;
    private String mes;
    private String anyo;


    public timeWorked() {
    }

    public timeWorked(String clave, String horaE,String horaS, String dia, String mes, String anyo) {
        this.clave = clave;
        this.horaEntrada = horaE;
        this.horaSalida = horaS;
        this.dia = dia;
        this.mes = mes;
        this.anyo = anyo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaE) {
        this.horaEntrada = horaE;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaS) {
        this.horaSalida = horaS;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnyo() {
        return anyo;
    }

    public void setAnyo(String anyo) {
        this.anyo = anyo;
    }
}
