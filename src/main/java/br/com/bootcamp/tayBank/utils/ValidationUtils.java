package br.com.bootcamp.tayBank.utils;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.caelum.stella.validation.CPFValidator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isCpfValid(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void validateCpf(String cpf) throws ServiceException {
        if(!ValidationUtils.isCpfValid(cpf)) {
            throw new ServiceException("CPF inválido");
        }
    }

    public static void validateEmail(String email) throws ServiceException {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        boolean matchFound = m.matches();

        if(!matchFound) {
            throw new ServiceException("Formato de email inválido");
        }
    }

    public static void validateAge(LocalDate data) throws ServiceException {
        LocalDate hoje = LocalDate.now();
        if (data.isAfter(hoje)) {
            throw new ServiceException("A data deve ser menor que a de hoje");
        }
        int age = (int) ChronoUnit.YEARS.between(data, hoje);
        if (age < 18)  {
            throw new ServiceException("O cliente deve ter mais de 18 anos");
        }
    }

    public static void validatePassword(String senha) throws ServiceException {
        Pattern p = Pattern.compile("(?=.*[}{,.^?~=+\\-_\\/*\\-+.\\|])(?=.*[a-zA-Z])(?=.*[0-9]).{8,}");
        Matcher m = p.matcher(senha);
        boolean matchFound = m.matches();

        if(!matchFound) {
            throw new ServiceException("Senha deve conter 8 caracteres, incluindo no mínimo 1 letra, 1 número e 1 caractere especial");
        }
    }
}
