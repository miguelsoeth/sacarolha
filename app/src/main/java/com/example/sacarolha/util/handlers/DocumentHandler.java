package com.example.sacarolha.util.handlers;

public class DocumentHandler {

    // Validate CPF
    public static boolean isValidCPF(String cpf) {
        // Remove non-numeric characters
        cpf = cpf.replaceAll("[^0-9]", "");

        // Check if CPF has 11 digits
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        int weight = 10;

        // Validate first digit
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }
        int firstCheckDigit = (sum * 10) % 11;
        if (firstCheckDigit == 10 || firstCheckDigit == 11) {
            firstCheckDigit = 0;
        }

        if (firstCheckDigit != (cpf.charAt(9) - '0')) {
            return false;
        }

        // Reset sum and weight for second digit
        sum = 0;
        weight = 11;

        // Validate second digit
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }
        int secondCheckDigit = (sum * 10) % 11;
        if (secondCheckDigit == 10 || secondCheckDigit == 11) {
            secondCheckDigit = 0;
        }

        return secondCheckDigit == (cpf.charAt(10) - '0');
    }

    // Validate CNPJ
    public static boolean isValidCNPJ(String cnpj) {
        // Remove non-numeric characters
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Check if CNPJ has 14 digits
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int sum = 0;
        int weight = 5;

        // Validate first digit
        for (int i = 0; i < 12; i++) {
            sum += (cnpj.charAt(i) - '0') * weight--;
            if (weight < 2) {
                weight = 9;
            }
        }
        int firstCheckDigit = 11 - (sum % 11);
        if (firstCheckDigit > 9) {
            firstCheckDigit = 0;
        }

        if (firstCheckDigit != (cnpj.charAt(12) - '0')) {
            return false;
        }

        // Reset sum and weight for second digit
        sum = 0;
        weight = 6;

        // Validate second digit
        for (int i = 0; i < 13; i++) {
            sum += (cnpj.charAt(i) - '0') * weight--;
            if (weight < 2) {
                weight = 9;
            }
        }
        int secondCheckDigit = 11 - (sum % 11);
        if (secondCheckDigit > 9) {
            secondCheckDigit = 0;
        }

        return secondCheckDigit == (cnpj.charAt(13) - '0');
    }

    // Validate CPF or CNPJ
    public static boolean isValidDocument(String document) {
        document = document.replaceAll("[^0-9]", "");
        return isValidCPF(document) || isValidCNPJ(document);
    }

    public static void main(String[] args) {
        // Example usage
        String cpf = "123.456.789-09";
        String cnpj = "12.345.678/0001-95";

        System.out.println("Is valid CPF? " + isValidCPF(cpf));
        System.out.println("Is valid CNPJ? " + isValidCNPJ(cnpj));
        System.out.println("Is valid document (CPF or CNPJ)? " + isValidDocument(cpf));
        System.out.println("Is valid document (CPF or CNPJ)? " + isValidDocument(cnpj));
    }
}
