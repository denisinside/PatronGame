package com.patron.game;

public class CardFactory {
    public static Card createCard(String cardName) {
        switch(cardName) {
            case "Удар лапою":
                return new AttackCard("Удар лапою", "enemyDamage шкоди ворогу", Rarity.COMMON, 1,6,"one",1);
            case "Бронежилет":
                return new SkillCard("Бронежилет",  "armor броні Патрону", Rarity.COMMON, 1,UseType.DEFENSE, "player",5);
            case "Шалений хвіст":
                return new AttackCard("Шалений хвіст","два рази б'є по enemyDamage шкоди ворогам", Rarity.RARE, 2, 7,"one",2);
            case "Бинт та ліки":
                return new SkillCard("Бинт та ліки",  "лікує Патрона 5 здоров'я",  Rarity.RARE,1,UseType.HEAL, "player",5);
            case "Широкий розмах":
                return new AttackCard("Широкий розмах", "enemyDamage шкоди всім ворогам та 1 вразливість", Rarity.COMMON, 1,3,"all", 1, new Effect[]{new VulnerabilityEffect(1)});
            case "Укус":
                return new AttackCard("Укус", "enemyDamage шкоди ворогу, 1 кровотеча в подарунок", Rarity.COMMON, 1,5,"one", 1, new Effect[]{new BleedingEffect(1)});
            case "Смаколик":
                return new SkillCard("Смаколик", "Надає 5 зцілення", Rarity.RARE, 1,UseType.HEAL, "player", new Effect[]{new CureEffect(5)});
            case "Маневр":
                return new AttackCard("Маневр", "enemyDamage шкоди ворогам, armor броні Патрону",  Rarity.COMMON,1,6,"one", 1);
            case "Банка огірків":
                return new AttackCard("Банка огірків", "enemyDamage шкоди ворогу та 2 вразливості", Rarity.COMMON, 2,9,"one", 1, new Effect[]{new VulnerabilityEffect(2)});
            case "Подвійний удар":
                return new AttackCard("Подвійний удар", "enemyDamage шкоди ворогу двічі",  Rarity.COMMON,1,5,"one", 2);
            case "Собаче панування":
                return new AttackCard("Собаче панування", "enemyDamage шкоди ворогу та 2 слабкості", Rarity.COMMON, 2,12,"one", 1, new Effect[]{new WeaknessEffect(2)});
            case "Хвіст-бумеранг":
                return new AttackCard("Хвіст-бумеранг", "enemyDamage шкоди випадковому ворогу тричі",  Rarity.LEGENDARY,1,3,"random", 3);
            case "Загострений хвіст":
                return new AttackCard("Загострений хвіст", "enemyDamage шкоди всім ворогам", Rarity.RARE, 1,7,"all", 1);
            case "Збити з ніг":
                return new AttackCard("Збити з ніг", "Завдаєш enemyDamage шкоди, береш карту з відбою на верх добору", Rarity.COMMON, 1,9,"one", 1);
            case "Безглуздий удар":
                return new AttackCard("Безглуздий удар", "enemyDamage шкоди, тасуєш у добір 1 запаморочення ", Rarity.RARE, 0,7,"one", 1);
            case "У слабке місце":
                return new AttackCard("У слабке місце", "enemyDamage шкоди, якщо ворог має вразливість отримуєш 1 енергію", Rarity.RARE, 1,5,"one", 1);
            case "Відчайдушний удар":
                return new AttackCard("Відчайдушний удар", "Завдаєш enemyDamage шкоди, тасуєш рану у добір", Rarity.COMMON, 1,12,"one", 1);
            case "Айкідо":
                return new AttackCard("Айкідо", "Завдаєш enemyDamage шкоди, добираєш 1 картку", Rarity.COMMON, 1,12,"one", 1);
            case "Різанина":
                return new AttackCard("Різанина", "Завдаєш enemyDamage шкоди", Rarity.COMMON, 2,20,"one", 1);

            default:
                throw new IllegalArgumentException("Недопустима назва карти: " + cardName);
        }
    }
    public static CurseCard getCurse(String cardName){
        switch (cardName){
            case "Зацикленість":
                return new CurseCard("Зацикленість", "Наприкінці ходу створює копію цієї картки на верху стопки добору");
            case "Сором":
                return new CurseCard("Сором", "Наприкінці ходу отримуєте 1 крихкість");
            case "Зрада":
                return new CurseCard("Зрада", "Наприкінці ходу отримуєте 1 вразливість");
            case "Безсилля":
                return new CurseCard("Безсилля", "Наприкінці ходу отримуєте 1 слабкість");
            case "Прокрастинація":
                return new CurseCard("Прокрастинація", "Наприкінці ходу втрачаєте 1 ОЗ за кожну карту в руці");
            case "Пуста миска":
                return new CurseCard("Пуста миска", "Сумуєте і наприкінці ходу отримуєте playerDamage шкоди");
            case "Травма":
                return new CurseCard("Травма", "Боляче, але жити можна");

            default:
                throw new IllegalArgumentException("Недопустима назва карти: " + cardName);
        }
    }
    public static StatusCard getStatus(String cardName){
        switch (cardName){
            case "Рана":
                return new StatusCard("Рана", "До кінця бою пройде", false);
            case "Запаморочення":
                return new StatusCard("Запаморочення", "Щось не так...", false);
            case "Опік":
                return new StatusCard("Опік", "Боляче! Наприкінці ходу отримаєш playerDamage шкоди", false);
            case "Слиз":
                return new StatusCard("Слиз", "Гидота...", true);
            case "Опромінювання":
                return new StatusCard("Опромінювання", "Наприкінці ходу отримаєш 2 радіації", true);

            default:
                throw new IllegalArgumentException("Недопустима назва карти: " + cardName);
        }
    }
}
