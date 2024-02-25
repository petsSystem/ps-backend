package br.com.petshop.pet.model.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum das raças de cachorros e gatos
 */
public enum BreedType {
    AFFENPINSCHER("1", "Affenpinscher", "P", "DOG"),
    AFGHAN_HOUND("2", "Afghan Hound", "GG", "DOG"),
    AIREDALE_TERRIER("3", "Airedale Terrier", "G", "DOG"),
    AKITA_AMERICANO("4", "Akita Americano", "G", "DOG"),
    AMERICAN_BULLY("5", "American Bully", "G", "DOG"),
    AMERICAN_STAFFORDSHIRE_TERRIER("6", "American Staffordshire Terrier", "G", "DOG"),
    BASENJI("6", "Basenji", "M", "DOG"),
    BASSET_HOUND("7", "Basset Hound", "M", "DOG"),
    BEAGLE("8", "Beagle", "G", "DOG"),
    BEARDED_COLLIE("9", "Bearded Collie", "G", "DOG"),
    BERNESE("10", "Bernese", "GG", "DOG"),
    BICHON_HAVANES("11", "Bichon Havanês", "M", "DOG"),
    BIEWER_TERRIER("12", "Biewer Terrier", "P", "DOG"),
    BLOODHOUND("13", "Bloodhound", "GG", "DOG"),
    BLUE_HEELER("14", "Blue Heeler", "G", "DOG"),
    BOERBOEL("15", "Boerboel", "GG", "DOG"),
    BORDER_COLLIE("16", "Border Collie", "G", "DOG"),
    BOSTON_TERRIER("17", "Boston Terrier", "P", "DOG"),
    BOXER("18", "Boxer", "GG", "DOG"),
    BRACO_ALEMAO("19", "Bernese", "G", "DOG"),
    BULLDOG("20", "Bernese", "P", "DOG"),
    BULLDOG_ALEMAO("21", "Bernese", "G", "DOG"),
    BULLDOG_AMERICANO("22", "Bernese", "M", "DOG"),
    BULLDOG_FRANCES("23", "Bulldog Francês", "M", "DOG"),
    BULLDOG_INGLES("24", "Bulldog Inglês", "G", "DOG"),
    BULLMASTIFF("25", "Bullmastiff", "GG", "DOG"),
    BULL_TERRIER("26", "Bull Terrier", "G", "DOG"),
    CAIRN_TERRIER("27", "Cairn Terrier", "P", "DOG"),
    CANE_CORSO("28", "Cane Corso", "GG", "DOG"),
    CAVALIER("29", "Cavalier King Charles Spaniel", "G", "DOG"),
    CHIHUAHUA("30", "Chihuahua", "P", "DOG"),
    CHOW_CHOW("31", "Chow Chow", "G", "DOG"),
    COCKER_SPANIEL("32", "Cocker Spaniel", "G", "DOG"),
    COCKER_SPANIEL_AMERICANO("33", "Cocker Spaniel Americano", "M", "DOG"),
    COCKER_SPANIEL_INCLES("34", "Cocker Spaniel Inglês", "M", "DOG"),
    COLLIE("35", "Collie", "GG", "DOG"),
    CORGI("36", "Corgi", "P", "DOG"),
    COTON_TULEAR("37", "Coton de Tulear", "P", "DOG"),
    DANCHSHUND("37", "Danchshund", "M", "DOG"),
    DALMATA("38", "Dálmata", "GG", "DOG"),
    DOBERMAN("39", "Doberman", "GG", "DOG"),
    DOGUE_ALEMAO("40", "Dogue Alemão", "GG", "DOG"),
    DOGUE_BORDEAUX("41", "Dogue de Bordeaux", "GG", "DOG"),
    FILA_BRASILEIRO("42", "Fila Brasileiro", "GG", "DOG"),
    FLAT_COATED_RETRIEVER("43", "Flat Coated Retriever", "G", "DOG"),
    FOX_PAULISTINHA("44", "Fox Paulistinha", "M", "DOG"),
    FOX_TERRIER_PELO_DURO("45", "Fox Terrier de Pelo Duro", "M", "DOG"),
    FOX_TERRIER_PELO_LISO("46", "Fox Terrier de Pelo Liso", "M", "DOG"),
    GOLDEN_RETRIEVER("47", "Golden Retriever", "G", "DOG"),
    GREYHOUND("48", "Greyhound", "GG", "DOG"),
    GRIFFON_BRUXELAS("49", "Griffon de Bruxelas", "P", "DOG"),
    HUSKY_SIBERIANO("50", "Husky Siberiano", "GG", "DOG"),
    JACK_RUSSEL_TERRIER("51", "Jack Russell Terrier", "P", "DOG"),
    KEESHOND("52", "Keeshond", "G", "DOG"),
    KOMONDOR("53", "Komondor", "GG", "DOG"),
    KUVASZ("54", "Kuvász", "GG", "DOG"),
    LABRADOR_RETRIEVER("55", "Labrador Retriever", "GG", "DOG"),
    LEAO_RODESIA("56", "Leão da Rodésia", "GG", "DOG"),
    LHASA_APSO("57", "Lhasa Apso", "P", "DOG"),
    LULU_POMERANIA("58", "Lulu da Pomerânia", "P", "DOG"),
    MALAMUTE_ALASCA("59", "Malamute do Alasca", "GG", "DOG"),
    MALTES("60", "Maltês", "P", "DOG"),
    MASTIFF("61", "Mastiff", "GG", "DOG"),
    MASTIM_ESPANHOL("62", "Mastim Espanhol", "GG", "DOG"),
    MASTIM_NAPOLITANO("63", "Mastim Napolitano", "GG", "DOG"),
    MASATIM_TIBETANO("64", "Mastim tibetano", "GG", "DOG"),
    NORFOLK_TERRIER("65", "Norfolk Terrier", "M", "DOG"),
    OLD_ENGLISH_SHEEPDOG("66", "Old English Sheepdog", "G", "DOG"),
    PASTOR_ALEMAO("67", "Pastor Alemão", "GG", "DOG"),
    PASTOR_AUSTRALIANO("68", "Pastor Australiano", "G", "DOG"),
    PASTOR_BELGA("69", "Pastor Belga", "GG", "DOG"),
    PASTOR_BELGA_GROENENDAEL("70", "Pastor Belga Groenendael", "G", "DOG"),
    PASTOR_BELGA_MALINOIS("71", "Pastor Belga Malinois", "GG", "DOG"),
    PASTOR_BEAUCE("72", "Pastor de Beauce", "G", "DOG"),
    PASTOR_SHETLAND("73", "Pastor de Shetland", "G", "DOG"),
    PASTOR_CAUCASO("74", "Pastor do Cáucaso", "GG", "DOG"),
    PASTOR_MAREMANO("75", "Pastor Maremano", "GG", "DOG"),
    PASTOR_SUICO("76", "Pastor Suíço", "GG", "DOG"),
    PEQUINES("77", "Pequinês", "P", "DOG"),
    PERDIGUEIRO("78", "Perdigueiro", "G", "DOG"),
    PINSCHER("79", "Pinscher", "P", "DOG"),
    PITBULL("80", "Pitbull", "G", "DOG"),
    POINTER_INGLES("81", "Pointer Inglês", "GG", "DOG"),
    POODLE("82", "Poodle", "G", "DOG"),
    PUG("83", "Pug", "M", "DOG"),
    PULI("84", "Puli", "M", "DOG"),
    RODESIA("85", "Rodésia", "GG", "DOG"),
    ROTTWEILER("86", "Rottweiler", "GG", "DOG"),
    SALUKI("87", "Saluki", "G", "DOG"),
    SAMOEIDA("88", "Samoeida", "G", "DOG"),
    SAO_BERNARDO("89", "São Bernardo", "GG", "DOG"),
    SCHIPPERKE("90", "Schipperke", "P", "DOG"),
    SCHNAUZER("91", "Schnauzer", "G", "DOG"),
    SCHNAUZER_GIGANTE("92", "Schnauzer Gigante", "GG", "DOG"),
    SCOTTISH_TERRIER("93", "Scottish Terrier", "P", "DOG"),
    SETTER_IRLANDES("94", "Setter Irlandês", "G", "DOG"),
    SHAR_PEI("95", "Shar Pei", "G", "DOG"),
    SHIBA_INU("96", "Shiba inu", "M", "DOG"),
    SHIH_TZU("97", "Shih Tzu", "M", "DOG"),
    SPANIEL_BRETAO("98", "Spaniel Bretão", "M", "DOG"),
    SPITZ_JAPONES("99", "Spitz Japonês", "M", "DOG"),
    TERRA_NOVA("100", "Terra Nova", "GG", "DOG"),
    TERRIER_TIBETANO("101", "Terrier Tibetano", "M", "DOG"),
    TERVUREN("102", "Tervuren", "G", "DOG"),
    TOSA_INU("103", "Tosa Inu", "GG", "DOG"),
    VIRA_LATA_DOG("104", "Vira-Lata", "M", "DOG"),
    WEIMARANER("105", "Weimaraner", "GG", "DOG"),
    WELSH_CORGI_CARDIGAN("106", "Welsh Corgi Cardigan", "M", "DOG"),
    WEST_HIGHLAND_TERRIER("107", "West Highland Terrier", "G", "DOG"),
    WHIPPET("108", "Whippet", "G", "DOG"),
    YORKSHIRE_TERRIER("109", "Yorkshire Terrier", "M", "DOG"),
    OUTROS_DOG("110", "Outros...", "", "DOG"),


    ABISSINIO("111", "Abissínio", "G", "CAT"),
    ANGORA("112", "Angorá", "G", "CAT"),
    BIRMANES("113", "Birmanês", "G", "CAT"),
    BENGAL("114", "Gato de Bengala", "M", "CAT"),
    MAINE_COON("115", "Maine Coon", "M", "CAT"),
    PERSA("116", "Persa", "M", "CAT"),
    RAGDOLL("117", "Ragdoll", "M", "CAT"),
    SCOTTISH_FOLD("118", "Scottish Fold", "M", "CAT"),
    SIAMES("119", "Siamês", "M", "CAT"),
    SPYNX("120", "Spynx", "M", "CAT"),
    VIRA_LATA_CAT("121", "Vira-Lata", "M", "CAT"),
    OUTROS_CAT("122", "Outros...", "", "CAT");

    private String id;
    private String name;
    private String size;
    private String type;

    BreedType(String id, String name, String size, String type) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.type = type;
    }

    public static String getName(BreedType breedType) {
        return breedType.name;
    }

    public static Size getSize(BreedType breedType) {
        return Size.valueOf(breedType.size);
    }
    public static List<BreedType> cats() {
        return Stream.of(BreedType.values())
                .filter(f -> f.type.equals("CAT"))
                .collect(Collectors.toList());
    }

    public static List<String> catValues() {
        return Stream.of(BreedType.values())
                .filter(f -> f.type.equals("CAT"))
                .map(m -> {
                    return m.name;
                })
                .collect(Collectors.toList());
    }

    public static List<String> dogValues() {
        return Stream.of(BreedType.values())
                .filter(f -> f.type.equals("DOG"))
                .map(m -> {
                    return m.name;
                })
                .collect(Collectors.toList());
    }

/*
    A


    Affenpinscher P dog 25 a 30 cm

    Afghan Hound GG dog 64 a 74 cm

    Airedale Terrier G dog 56 a 61 cm

    Akita Americano G dog 61 a 71 cm

    American Bully G dog 41 a 51cm

    American Staffordshire Terrier G dog 42 a 49 cm

    Abissínio G cat 50 cm a 60 cm

    Angorá G cat 50cm e 60cm


    B


    Basenji M dog 35 a 44 cm

    Basset Hound M dog 33 a 38 cm

    Beagle G dog 33 a 38cm

    Bearded Collie G dog 51 a 56 cm

    Bernese GG dog 58 a 70 cm

    Bichon Havanês M dog 21 a 29 cm

    Biewer Terrier P dog 16 a 28 cm

    Bloodhound GG dog 58 a 72 cm

    Blue Heeler G dog 43 a 51 cm

    Boerboel GG dog 55 a 70 cm

    Border Collie G dog 46cm a 56cm

    Boston Terrier P dog 38 a 43cm

    Boxer GG dog 53cm a 65cm

    Braco Alemão G dog 57 a 68 cm

    Bulldog P dog 30 a 38 cm

    Bulldog Alemão G dog 40 a 70 cm

    Bulldog Americano M dog 50 a 52 cm

    Bulldog Francês M dog 24 a 35 cm

    Bulldog Inglês G dog 35 a 40 cm

    Bullmastiff GG dog 61 a 69 cm

    Bull Terrier G dog 53 a 56 cm

    Birmanês G cat 50cm e 60cm

    Bengal ou Gato de Bengala M cat 25cm e 35cm


    C


    Cairn Terrier P dog 28 a 31 cm

    Cane Corso GG dog 60cm a 68cm

    Cavalier King Charles Spaniel G dog 30 a 33 cm

    Chihuahua P dog 18cm a 25cm

    Chow Chow G dog 45cm a 55cm

    Cocker Spaniel G dog 38 a 43 cm

    Cocker Spaniel Americano M dog 34 a 39 cm

    Cocker Spaniel Inglês M dog 36 a 39 cm

    Collie GG dog 55cm a 65cm

    Corgi P dog 25 a 35 cm

    Coton de Tulear P dog 22 a 30 cm


    D


    Danchshund M dog 20 a 22cm

    Dálmata dog GG 54 a 62

    Doberman GG dog 63 a 72 cm

    Dogue Alemão GG 72cm a 90cm

    Dogue de Bordeaux GG dog 57 a 70 cm


    F


    Fila Brasileiro GG dog 65cm a 75cm

    Flat Coated Retriever G dog 56 a 62 cm

    Fox Paulistinha M dog 33 a 40 cm

    Fox Terrier de Pelo Duro M dog 36 a 39 cm

    Fox Terrier de Pelo Liso M dog 35 a 39 cm


    G


    Golden Retriever G dog 51 a 61 cm

    Greyhound GG dog 67 a 77 cm

    Griffon de Bruxelas P dog 28cm


    H


    Husky Siberiano GG dog 55 a 65 cm

    J


    Jack Russell Terrier P dog 25 a 30 cm


    K


    Keeshond G dog 43 a 55 cm

    Komondor GG dog 65 a 65 cm

    Kuvász GG dog 65cm a 75cm


    L


    Labrador Retriever GG dog 60 cm

    Leão da Rodésia GG dog 61 a 69 cm

    Lhasa Apso P dog 22cm a 25cm

    Lulu da Pomerânia P dog 20 a 49cm


    M


    Malamute do Alasca GG dog 57 a 71 cm

    Maltês P dog 20 a 25cm

    Mastiff GG dog 70 a 80 cm

    Mastim Espanhol GG dog 72 a 80 cm

    Mastim Napolitano GG dog 60 a 75 cm

    Mastim tibetano GG dog 60 a 76 cm

    Maine Coon ? cat 25cm e 41cm


    N


    Norfolk Terrier M dog 23 a 26 cm


    O


    Old English Sheepdog G dog 56 a 62 cm


    P


    Pastor Alemão GG dog 55cm - 65cm

    Pastor Australiano G dog 46 a 58 cm

    Pastor Belga GG dog 56 a 66cm

    Pastor Belga Groenendael G dog 56 a 66 cm

    Pastor Belga Malinois GG dog 55 a 68 cm

    Pastor de Beauce G dog 61 a 70 cm

    Pastor de Shetland G dog 30 a 39 cm

    Pastor do Cáucaso GG dog 64 a 75 cm

    Pastor Maremano Abruzês GG dog 60 a 73 cm

    Pastor Suíço GG dog 60cm a 70cm

    Pequinês P dog Até 20 cm

    Perdigueiro G dog 48 a 62 cm

    Pinscher P dog 20 a 30 cm

    Pitbull G dog 40cm a 48cm

    Pointer Inglês GG dog 61 a 69 cm

    Poodle G dog 24cm a 60cm

    Pug M dog 20cm - 30cm

    Puli M dog 36 a 43 cm

    Persa ? cat 20cm e 25cm


    R


    Rodésia GG dog 61cm a 69cm

    Rottweiler GG dog 56 a 69 cm

    Ragdoll ? cat 50cm e 60cm


    S


    Saluki G dog 51 a 78 cm

    Samoeida G dog 53cm a 60cm

    São Bernardo GG dog 65 a 80 cm

    Schipperke P dog 25 a 35 cm

    Schnauzer G dog 30 a 70 cm

    Schnauzer Gigante GG dog 60 a 70 cm

    Scottish Terrier P dog 25 a 28 cm

    Setter Irlandês G dog 55 a 67 cm

    Shar Pei G dog 46 a 51 cm

    Shiba inu M dog 33 a 43 cm

    Shih Tzu M dog 20 a 28 cm

    Spaniel Bretão M dog 46 a 52 cm

    Spitz Japonês M dog 30 a 38 cm

    Scottish Fold ? cat 50cm e 60cm

    Siamês ? cat 20cm e 30cm

    Spynx ? cat 50cm e 60cm


    T


    Terra Nova ou Newfie GG dog 63 a 71 cm

    Terrier Tibetano M dog 35 43 cm

    Tervuren G dog 56 a 66 cm

    Tosa Inu GG dog 55 a 60 cm


    V

    Vira-Lata M


    W


    Weimaraner GG dog 58 a 68 cm

    Welsh Corgi Cardigan M dog 25 a 30 cm

    West Highland Terrier G dog 23cm a 27cm

    Whippet G dog 44 a 51 cm

    Y


    Yorkshire Terrier M dog Até 22cm

    */
}
