package pl.banksystem.logic.account.generators;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class IdGenerator implements IdentifierGenerator {

    public static final int codeLength = 10;

    public static Long generateID() {
        log.info("Generating id");
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            log.error("Generation Failed: %s", e);
            e.printStackTrace();
        }
        IntStream intStream = secureRandom.ints(codeLength, 0, 24);
        int[] code = intStream.toArray();
        return Long.parseLong(Arrays.toString(code).replaceAll("\\[|\\]|,|\\s", ""));
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Long generatedID = generateID();
        List list = session.createQuery("select accountID from Account").getResultList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == generatedID) generatedID = generateID();
        }
        return generatedID;
    }
}

