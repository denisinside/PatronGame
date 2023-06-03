package com.patron.game;

public class Bandit extends Enemy{
    public Bandit(){
        super(random.nextInt(6) + 30);
        name = "Бандит";

        Attack[]attacks = {
                 new Attack(random.nextInt(4) + 6),
                 new Attack(random.nextInt(4) + 8),
                 new Attack( random.nextInt(4) + 7) };
         Defend[]blocks = {
                 new Defend(random.nextInt(3)+8),
                 new Defend(random.nextInt(4)+10) };
         shuffleMoves(attacks,blocks);
    }
}
class RadioactiveRat extends Enemy{
    public RadioactiveRat(){
        super( random.nextInt(7) + 27);
        name = "Радіоактивний щур";
        Attack[]attacks = {
                new Attack(random.nextInt(1) + 1, new Effect[]{new RadiationEffect(3)}),
                new Attack(random.nextInt(2) + 1, new Effect[]{new RadiationEffect(2)}),
                new Attack( random.nextInt(1) + 2, new Effect[]{new RadiationEffect(3)}) };
        Defend[]blocks = {
                new Defend(random.nextInt(2)+2, new Effect[]{new CureEffect(this,5)}),
                new Defend(random.nextInt(2)+3, new Effect[]{new CureEffect(this,5)}) };
        shuffleMoves(attacks,blocks);

    }

}

class FireBagSoldier extends Enemy{
    public FireBagSoldier(){
        super(random.nextInt(5) + 14);
        name = "Солдатик-солдат";
        Attack[]attacks = {
                new Attack(random.nextInt(4) + 5),
                new Attack(random.nextInt(5) + 6)};
        Defend[]blocks = {
                new Defend(random.nextInt(4)+5)};
        shuffleMoves(attacks,blocks);

    }
}
class FireBagMedic extends Enemy{
    public FireBagMedic(){
        super(random.nextInt(4) + 12);
        name = "Солдатик-медик";
        Attack[] attacks = {
                new Attack(random.nextInt(3) + 3, new Effect[]{new WeaknessEffect(2)})};
        Impact[] impacts = {
                new Impact(new Effect[]{new CureEffect(this, 4)}, true),
                new Impact(new Effect[]{new FragilityEffect(2)}),
                new Impact(new Effect[]{new WeaknessEffect(2)})};

        shuffleMoves(attacks, impacts);
    }
}
class FireBagTank extends Enemy {
    public FireBagTank() {
        super(random.nextInt(3) + 10);
        name = "Солдатик-броневик";

        enemyMoves.add(new Defend(random.nextInt(4)+4));
        enemyMoves.add(new Defend(random.nextInt(2)+3,true));
    }
}
class FireBagSniper extends Enemy {
    public FireBagSniper() {
        super(random.nextInt(3) + 13);
        name = "Солдатик-снайпер";

        enemyMoves.add(new Defend(random.nextInt(4)+5));
        enemyMoves.add(new Impact());
        enemyMoves.add(new Attack(15));
    }
}