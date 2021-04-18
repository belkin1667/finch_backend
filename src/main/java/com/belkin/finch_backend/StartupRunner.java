package com.belkin.finch_backend;

import com.belkin.finch_backend.dao.interfaces.*;
import com.belkin.finch_backend.model.*;
import com.belkin.finch_backend.security.ApplicationUserRole;
import com.belkin.finch_backend.security.dao.ApplicationUserDAO;
import com.belkin.finch_backend.security.model.ApplicationUser;
import com.belkin.finch_backend.util.Base62;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.belkin.finch_backend.security.ApplicationUserRole.USER;

@Component
@Order(2)
public class StartupRunner implements ApplicationRunner {

    @Autowired
    public StartupRunner(@Qualifier("database_user") UserDAO userDAO,
                         @Qualifier("database_guide") GuideDAO guideDAO,
                         @Qualifier("database_card") CardDAO cardDAO,
                         @Qualifier("database_image") ImageMetadataDAO imageDAO,
                         @Qualifier("database_subs") SubsDAO subsDAO,
                         @Qualifier("database_guide_favour") GuideFavourDAO guideFavorDAO,
                         @Qualifier("database_guide_like") GuideLikeDAO guideLikeDAO,
                         @Qualifier("database_appuser") ApplicationUserDAO applicationUserDAO,
                         PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.guideDAO = guideDAO;
        this.cardDAO = cardDAO;
        this.imageDAO = imageDAO;
        this.subsDAO = subsDAO;
        this.guideFavorDAO = guideFavorDAO;
        this.guideLikeDAO = guideLikeDAO;
        this.applicationUserDAO = applicationUserDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createImageDirectory();
        //emptyImageDirectory();
        //generateMockData();
        getClassesSpec();
    }


    private void getClassesSpec() throws IOException {
        String path = "." + File.separator + "classes";
        if (!Files.exists(Paths.get(path)))
            Files.createDirectory(Paths.get(path));

        Files.list(Paths.get(path))
                .map(filename -> {
                    try {
                        return Files.deleteIfExists(filename);
                    } catch (IOException e) {
                        return false;
                    }
                });

        String linesep = "\n";

        //scan urls that contain 'my.package', include inputs starting with 'my.package', use the default scanners
        Reflections reflections = new Reflections("com.belkin.finch_backend",
                new SubTypesScanner(false));
        Set<Class<? extends Object>> classes = reflections.getSubTypesOf(Object.class);
        for (var c : classes) {
            String res = "Класс," + c.getName() + linesep;
            List<String> constructors = Arrays.stream(c.getDeclaredConstructors())
                    .map(constructor -> {
                        String accessModifier = Modifier.toString(constructor.getModifiers());
                        String name = c.getSimpleName();
                        List<String> params = Arrays.stream(constructor.getParameterTypes())
                                .map(Class::getSimpleName)
                                .collect(Collectors.toList());
                        return name + "," + accessModifier + "," + String.join("; ", params) + ", ";
                    }).collect(Collectors.toList());
            res += "Конструкторы" + linesep;
            res += "Имя, Модификатор, Аргументы, Назначение" + linesep;
            res += String.join(linesep, constructors) + linesep;
            List<String> methods = Arrays.stream(c.getDeclaredMethods())
                    .map(method -> {
                        String accessModifier = Modifier.toString(method.getModifiers());
                        String returnType = method.getReturnType().getSimpleName();
                        String name = method.getName();
                        List<String> params = Arrays.stream(method.getParameters())
                                .map(p -> p.getType().getSimpleName())
                                .collect(Collectors.toList());
                        return name + "," + accessModifier + "," + returnType + "," + String.join("; ", params) + ", ";
                    })
                    .collect(Collectors.toList());
            res += "Методы" + linesep;
            res += "Имя, Модификатор, Тип возвращаемого значения, Аргументы, Назначение" + linesep;
            res += String.join(linesep, methods) + linesep;
            List<String> fields = Arrays.stream(c.getDeclaredFields())
                    .map(field -> {
                        String name = field.getName();
                        String accessModifier = Modifier.toString(field.getModifiers());
                        String type = field.getType().getSimpleName();
                        return name + "," + accessModifier + "," + type + ", ";
                    })
                    .collect(Collectors.toList());
            res += "Поля" + linesep;
            res += "Имя, Модификатор, Тип, Назначение" + linesep;
            res += String.join(linesep, fields) + linesep;

            String fileName = path +  File.separator + c.getName() + ".csv";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(res);
            writer.close();
        }
    }
    /*
       1. split s by ' '
          s[0] - modifier
          s[1] - constructor
       2. split s[1] by '('
          str[0] - constructor name
          str[1] - params
       3. delete ')' in str[1]
       4. split str[1] by ','
       5. str[0], & str[1].

     */
    //public com.belkin.finch_backend.model.Favour(java.lang.Integer,java.lang.String,java.lang.String)


    private final UserDAO userDAO;
    private final GuideDAO guideDAO;
    private final CardDAO cardDAO;
    private final ImageMetadataDAO imageDAO;
    private final SubsDAO subsDAO;
    private final GuideLikeDAO guideLikeDAO;
    private final GuideFavourDAO guideFavorDAO;
    private final ApplicationUserDAO applicationUserDAO;
    private final PasswordEncoder passwordEncoder;

    private void generateMockData() {
        List<User> users = new ArrayList<>();
        List<ApplicationUser> appusers = new ArrayList<>();
        List<Guide> guides = new ArrayList<>();
        List<ImageMetadata> images = new ArrayList<>();
        List<Card> cards = new ArrayList<>();
        List<Subscription> subs = new ArrayList<>();


        Set<ApplicationUserRole> userRoleSet = new HashSet<>();
        userRoleSet.add(USER);


        List<Content> content = new ArrayList<>();
        content.add(new Content("text", "Aliquam dui enim, maximus a purus convallis, rhoncus dapibus nibh. Nulla at justo id dolor placerat dignissim. Etiam viverra finibus tincidunt. Fusce dictum convallis arcu ac dapibus. Sed vulputate sodales elit, sit amet tristique justo ultricies quis. Nullam tempor, libero non congue cursus, massa diam luctus turpis, vitae molestie neque tellus vitae est. Proin varius, ipsum imperdiet pulvinar euismod, sem urna molestie nulla, dignissim venenatis elit nisl nec diam. Cras rhoncus iaculis finibus. Ut velit elit, faucibus et augue imperdiet, ornare scelerisque augue. Praesent pretium risus a libero venenatis ornare. Praesent auctor ipsum efficitur sem ultricies fermentum. Morbi venenatis mi et leo faucibus dignissim. Sed maximus feugiat massa, quis dapibus augue porta et."));
        content.add(new Content("image", "2cye96E6YMY"));
        content.add(new Content("text", "Etiam pretium a magna ut efficitur. Donec leo felis, tempus a sapien quis, maximus vulputate augue. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque condimentum dui ac arcu interdum, a pulvinar massa pellentesque. Quisque scelerisque semper finibus. Sed vitae dignissim tellus, quis viverra velit. Nunc vel pellentesque eros, id hendrerit nunc. Donec finibus semper nunc sit amet vulputate. "));
        content.add(new Content("text", " Fusce venenatis sagittis neque a malesuada. Suspendisse eu malesuada odio. Suspendisse auctor dui mi, eget vehicula odio congue et. Nunc a vestibulum ipsum. Curabitur aliquam tellus felis, sed pellentesque lectus viverra at. Nulla mattis aliquam ligula vel condimentum. Etiam rutrum nisl orci, nec posuere est aliquam quis. Donec sit amet dolor scelerisque, tristique neque eu, mattis massa."));
        content.add(new Content("image", "sYfqTArhNU4"));

        users.add(new User("mike",
                "mike@finch.com",
                "8908088399",
                "Genius, traveler, photographer",
                "Love photography and traveling in Europe",
                "J2o89z1DFMj",
                User.ProfileAccess.ALL));
        appusers.add(new ApplicationUser("mike", "mike@finch.com", passwordEncoder.encode("password"),
                userRoleSet, true, true, true, true));

        users.add(new User("johndoe",
                "john@finch.com",
                "8908088399",
                "Travel agent. See my website john-travel.com",
                "You can offer a tour in my travel agency. We can arrange any kind of the trip. We can make your dreams come true",
                "2l0yHF5D6rW",
                User.ProfileAccess.ALL));
        appusers.add(new ApplicationUser("johndoe", "john@finch.com", passwordEncoder.encode("password"),
                userRoleSet, true, true, true, true));

        users.add(new User("donald",
                "don@finch.com",
                "87306649306640",
                "Former US President. trump-hotels.org",
                "Make my hotels great again! Visit my hotels, link in bio",
                "tm7ghZDWKoB",
                User.ProfileAccess.ALL));
        appusers.add(new ApplicationUser("donald", "don@finch.com", passwordEncoder.encode("password"),
                userRoleSet, true, true, true, true));

        users.add(new User("elon",
                "elon@finch.com",
                "109365936965",
                "Genius, billionaire, playboy, philanthropist",
                "SpaceX now offers Moon travel, to buy a tour see SpaceX website",
                "4SEa9DjdiXW",
                User.ProfileAccess.ALL));
        appusers.add(new ApplicationUser("elon", "elon@finch.com", passwordEncoder.encode("password"),
                userRoleSet, true, true, true, true));

        subs.add(new Subscription("mike", "elon"));
        subs.add(new Subscription("mike", "johndoe"));
        subs.add(new Subscription("mike", "donald"));

        subs.add(new Subscription("johndoe", "elon"));
        subs.add(new Subscription("johndoe", "mike"));
        subs.add(new Subscription("johndoe", "donald"));

        subs.add(new Subscription("donald", "elon"));
        subs.add(new Subscription("donald", "mike"));
        subs.add(new Subscription("donald", "johndoe"));

        subs.add(new Subscription("elon", "donald"));
        subs.add(new Subscription("elon", "mike"));
        subs.add(new Subscription("elon", "johndoe"));


        images.add(new ImageMetadata("mike", new Base62("J2o89z1DFMj")));
        images.add(new ImageMetadata("johndoe", new Base62("2l0yHF5D6rW")));
        images.add(new ImageMetadata("donald", new Base62("tm7ghZDWKoB")));
        images.add(new ImageMetadata("elon", new Base62("4SEa9DjdiXW")));
        images.add(new ImageMetadata("mike", new Base62("WnvxVoFmXgZ")));
        images.add(new ImageMetadata("mike", new Base62("sYfqTArhNU4")));
        images.add(new ImageMetadata("mike", new Base62("WrT6X9L2sSp")));
        images.add(new ImageMetadata("mike", new Base62("sUHNzibZiwf")));
        images.add(new ImageMetadata("mike", new Base62("2cye96E6YMY")));


        guides.add(new Guide("mike",
                new Base62("XgKGSVikuD2"),
                "Euro tour. France",
                "Съешь ещё этих мягких французских булок, да выпей чаю",
                "France",
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                "sYfqTArhNU4", List.of("Europe", "France")
        ));

        Collections.shuffle(content);

        cards.add(new Card(new Base62("duLzlQcGeHV"),
                new Base62("XgKGSVikuD2"),
                "sUHNzibZiwf",
                "Rome",
                "Italy",
                content
        ));

        Collections.shuffle(content);
        cards.add(new Card(new Base62("SboCW3kK4sn"),
                new Base62("XgKGSVikuD2"),
                "2cye96E6YMY",
                "Florence",
                "Italy",
                content
        ));

        guides.add(new Guide("mike",
                new Base62("ACcvoVo0M58"),
                "USA West Coast",
                "The Story of my USA travel this summer",
                "USA",
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                "WnvxVoFmXgZ",
                List.of("USA", "NYC")
        ));

        Collections.shuffle(content);
        cards.add(new Card(new Base62("ItwAnV4dips"),
                new Base62("ACcvoVo0M58"),
                "WrT6X9L2sSp",
                "Washington",
                "USA",
                content
        ));

        Collections.shuffle(content);
        cards.add(new Card(new Base62("FnGK1ymFvCR"),
                new Base62("ACcvoVo0M58"),
                "sUHNzibZiwf",
                "New York",
                "USA",
                content
        ));

        Collections.shuffle(content);
        cards.add(new Card(new Base62("pArb01rdFt2"),
                new Base62("ACcvoVo0M58"),
                "2cye96E6YMY",
                "Florida",
                "USA",
                content
        ));

        guides.add(new Guide("johndoe",
                new Base62("int7gu7yFtA"),
                "Euro tour. France",
                "Съешь ещё этих мягких французских булок, да выпей чаю",
                "France",
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                "2cye96E6YMY",
                List.of("Europe", "France")
        ));

        Collections.shuffle(content);
        cards.add(new Card(new Base62("DQGm58bvpSp"),
                new Base62("int7gu7yFtA"),
                "sUHNzibZiwf",
                "Rome",
                "Italy",
                content
        ));

        guides.add(new Guide("elon",
                new Base62("hpM8wkYWrNb"),
                "Euro tour. France",
                "Съешь ещё этих мягких французских булок, да выпей чаю",
                "France",
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                "2cye96E6YMY",
                List.of("Europe", "France")
        ));

        Collections.shuffle(content);
        cards.add(new Card(new Base62("hkBRrXIoSef"),
                new Base62("hpM8wkYWrNb"),
                "sUHNzibZiwf",
                "Rome",
                "Italy",
                content
        ));


        List<Like> likes = new ArrayList<>();
        likes.add(new Like("mike", new Base62("int7gu7yFtA")));
        likes.add(new Like("mike", new Base62("hpM8wkYWrNb")));
        likes.add(new Like("elon", new Base62("XgKGSVikuD2")));
        likes.add(new Like("elon", new Base62("ACcvoVo0M58")));
        likes.add(new Like("johndoe", new Base62("XgKGSVikuD2")));
        likes.add(new Like("donald", new Base62("XgKGSVikuD2")));

        List<Favour> favourites = new ArrayList<>();
        favourites.add(new Favour("elon", new Base62("int7gu7yFtA")));
        favourites.add(new Favour("elon", new Base62("XgKGSVikuD2")));
        favourites.add(new Favour("elon", new Base62("ACcvoVo0M58")));

        for (User user : users) {
            userDAO.save(user);
        }
        for (ApplicationUser user : appusers) {
            applicationUserDAO.save(user);
        }
        for (ImageMetadata image : images) {
            imageDAO.save(image);
        }
        for (Guide guide : guides) {
            guideDAO.save(guide);
        }
        for (Card card : cards) {
            cardDAO.save(card);
        }
        for (Subscription sub : subs) {
            subsDAO.save(sub);
        }
        for (Like like : likes) {
            guideLikeDAO.save(like);
        }
        for (Favour favor : favourites) {
            guideFavorDAO.save(favor);
        }
    }

    private void createImageDirectory() throws IOException {
        if (!Files.exists(Paths.get(ImageMetadata.BASE_PATH)))
            Files.createDirectory(Paths.get(ImageMetadata.BASE_PATH));
    }

    private void emptyImageDirectory() throws IOException {
        Files.list(Paths.get(ImageMetadata.BASE_PATH))
                .map(path -> {
                    try {
                        return Files.deleteIfExists(path);
                    } catch (IOException e) {
                        return false;
                    }
                });
    }
}
