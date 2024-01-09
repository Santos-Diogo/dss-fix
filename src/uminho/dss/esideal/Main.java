/*
 *  DISCLAIMER: Este código foi criado para discussão e edição durante as aulas práticas de DSS, representando
 *  uma solução em construção. Como tal, não deverá ser visto como uma solução canónica, ou mesmo acabada.
 *  É disponibilizado para auxiliar o processo de estudo. Os alunos são encorajados a testar adequadamente o
 *  código fornecido e a procurar soluções alternativas, à medida que forem adquirindo mais conhecimentos.
 */
package uminho.dss.esideal;

/**
 *
 * @author DSS
 * @version 20201206
 */
public class Main {

    /**
     * O método main cria a aplicação e invoca o método run()
     */
    public static void main(String[] args) {
        if (args.length == 0)
        {
            System.out.println("Please use command-line argument to choose mode:\n1 - System Manager\n2 - Frontdesk Employee\n3 - Mechanic");
            return;
        }
        System.out.println(args[0]);
        try {
            switch (args[0]) {
                case "1":
                    new uminho.dss.esideal.ui.SM.TextUI().run();
                    break;
                case "2":
                    new uminho.dss.esideal.ui.FE.TextUI().run();
                    break;
                case "3":
                    //new uminho.dss.esideal.ui.M.TextUI().run();
                    break;
                default:
                    System.out.println("Invalid option '"+args[0]+"'. Valid options:\n1 - System Manager\n2 - Frontdesk Employee\n3 - Mechanic" );
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Error: "+e.getMessage()+" ["+ e +"]");
        }
    }


}
