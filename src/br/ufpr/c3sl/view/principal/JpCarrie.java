package br.ufpr.c3sl.view.principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import br.ufpr.c3sl.dao.MistakeDAO;
import br.ufpr.c3sl.daoFactory.DAOFactory;
import br.ufpr.c3sl.deepClone.ObjectByteArray;
import br.ufpr.c3sl.exception.UserException;
import br.ufpr.c3sl.model.Mistake;
import br.ufpr.c3sl.model.MistakeInfo;
import br.ufpr.c3sl.model.User;
import br.ufpr.c3sl.util.Util;
import br.ufpr.c3sl.view.footer.JpMenuBarFooter;
import br.ufpr.c3sl.view.user.InitialDialog;
import br.ufpr.c3sl.view.util.ImageButton;

/**
 * CARRIE Framework
 * class JpCarrie the main panel of CARRIE framework
 * @author  Diego Marczal
 * @version Outubro 16, 2010
 */ 
@SuppressWarnings("serial")
public class JpCarrie extends JPanel{

	private JPanel jpHeader;
	private JPanel jpBody;
	private JPanel jpMain;
	private JPanel jpFooter;
	private JPanel jpFontSize;

	private JpMenuBarFooter jpMenuFooter;

	private static final Border BORDER = BorderFactory.createEtchedBorder();

	private static JpCarrie carrie = new JpCarrie();

	private static Font FONT = new Font("Arial", Font.PLAIN, 6);
	private static Font FONT_TITLE = new Font("Arial", Font.BOLD, 14);
	
	private JLabel lbTitle;

	private ImageButton upSizeLetter;
	private ImageButton downSizeLetter;
	private ImageButton originalSizeLetter;

	public static JpCarrie getInstance(){
		return carrie;
	}

	public static void newInstance(){
		carrie = new JpCarrie();
	}
	
//	private void resetCarrie(){
//		jpHeader = new JPanel(new BorderLayout());
//		jpBody = new JPanel(new BorderLayout());
//		jpFooter  = new JPanel();
//		jpMenuFooter = new JpMenuBarFooter();
//		jpFontSize = new JPanel();
//		create();
//	}
	
	/**
	 * Private Constructor , initialize variables
	 */ 
	private JpCarrie(){
		InitialDialog.showDialog();
		jpHeader = new JPanel(new BorderLayout());
		jpBody = new JPanel(new BorderLayout());
		jpFooter  = new JPanel();
		jpMenuFooter = new JpMenuBarFooter();
		jpFontSize = new JPanel();
		create();
	}

	/*
	 * Create Component JpCarrie
	 */
	private void create(){
		configure();
		addComponents();
		addBorder();
	}

	/*
	 * Configure layout of JpCarrie
	 */
	private void configure(){
		setLookAndFell();
		this.setLayout(new BorderLayout());
		configureFooter();
		configureHeader();
		configTitle();
		addFontSizeButtos();
	}

	/*
	 * Add Components in JpCarrie
	 */
	private void addComponents(){
		this.add(jpHeader, BorderLayout.NORTH);
		this.add(jpBody, BorderLayout.CENTER);
		this.add(jpFooter, BorderLayout.SOUTH);
	}


	private void addBorder(){
		configureBorder(this);
		configureBorder(jpHeader);
		configureBorder(jpBody);
		configureBorder(jpFooter);
	}

	private void configureHeader(){
		JPanel jpInfo = new JPanel();
		jpInfo.setLayout(new BoxLayout(jpInfo, BoxLayout.Y_AXIS));

		String email = "";
		String mode =  "";

		User user = Util.getCurrentUserl();
		if(user != null){
			email = user.getEmail();
			mode =  user.getMode();	
		}

		JLabel lbemail = new JLabel("Olá " + email);
		lbemail.setFont(FONT);
		lbemail.setForeground(Color.red);
		JLabel lbmode = new JLabel("Modo e execução " + mode);
		lbmode.setFont(FONT);
		lbmode.setForeground(Color.red);

		jpInfo.add(lbemail);
		jpInfo.add(lbmode);
		
		jpHeader.add(jpInfo, BorderLayout.WEST);
	}

	private void configTitle() {
		JPanel jpTitle = new JPanel();
		lbTitle = new JLabel("Default Title");
		lbTitle.setFont(FONT_TITLE);
		jpTitle.add(lbTitle);
		jpHeader.add(jpTitle, BorderLayout.CENTER);
	}

	private void updateTitle(){
		String parte = jpMain.getName().split(":")[0];
		lbTitle.setText(this.getName() + " - " + parte);
	}

	private void addFontSizeButtos() {
		originalSizeLetter = new ImageButton(Util.getIconURL(getClass(), "normal_up"),
				Util.getIconURL(getClass(), "normal_down"));
		originalSizeLetter.setName("originalSizeLetter");
		originalSizeLetter.setToolTipText("Restaurar fonte padrão");
		jpFontSize.add(originalSizeLetter);

		downSizeLetter = new ImageButton(Util.getIconURL(getClass(), "minus_up"),
				Util.getIconURL(getClass(), "minus_down"));
		downSizeLetter.setName("downSizeLetter");
		downSizeLetter.setToolTipText("Diminuir fonte");
		jpFontSize.add(downSizeLetter);

		upSizeLetter = new ImageButton(Util.getIconURL(getClass(), "plus_up"),
				Util.getIconURL(getClass(), "plus_down"));
		upSizeLetter.setName("upSizeLetter");
		upSizeLetter.setToolTipText("Aumentar fonte");
		jpFontSize.add(upSizeLetter);

		jpHeader.add(jpFontSize, BorderLayout.EAST);
	}

	/*
	 * Configure border of a JPanel
	 * @param JPanel jpanel
	 */
	private void configureBorder(JPanel jpanel){
		jpanel.setBorder(BORDER);
	}

	private void configureFooter(){
		jpFooter.add(jpMenuFooter);
	}

	private void setLookAndFell() {
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//	UIManager.getCrossPlatformLookAndFeelClassName());
			//	UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  update main panel
	 *  @param JPanel newPanel new main panel
	 */
	public void updateMainPanel(JPanel newPanel){
		if (jpMain != null)
			this.jpBody.remove(jpMain);

		this.jpMain = newPanel;
		this.jpBody.add(jpMain, BorderLayout.CENTER);

		updateTitle();
		SwingUtilities.updateComponentTreeUI(jpBody);		
	}

	/**
	 *  add a panel to the paginator
	 *  @param String the identifier panel's name
	 *  @param JPanel panel to be added
	 */
	public void addPanel(String name, JPanel panel){
		panel.setName(name);
		jpMenuFooter.addPanelToPaginator(panel);
	}

	/**
	 *  Save the main panel state 
	 *  @param mistake information about the mistake
	 */
	public void saveState(final MistakeInfo mistakeInfo){
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				Mistake mistake = new Mistake();
				mistake.setExercise(jpMain.getName());
				mistake.setLearningObject(JpCarrie.this.getName());
				mistake.setMistakeInfo(mistakeInfo);
				mistake.setObject(ObjectByteArray.getByteOfArray(jpMain));
				mistake.setUser(Util.getCurrentUserl());

				DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.DATABASE_CHOOSE);
				MistakeDAO mistakedao = dao.getMistakeDAO();

				try {
					mistakedao.insert(mistake);
					jpMenuFooter.addErrorToMenu(mistake);
				} catch (UserException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void loadDataFromBD() {
		DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.DATABASE_CHOOSE);
		MistakeDAO mistakedao = dao.getMistakeDAO();

		List<Mistake> list = mistakedao.getAll(Util.getCurrentUserl(), this.getName());

		System.out.println(list.size());
		
		for (Mistake mistake : list) {
			jpMenuFooter.addErrorToMenu(mistake);
		}
	}

}