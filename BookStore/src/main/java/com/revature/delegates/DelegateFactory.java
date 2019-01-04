package com.revature.delegates;

public class DelegateFactory {
	private static DelegateFactory df;
	private BookDelegate bd= new BookDelegate();
	private AuthorDelegate ad= new AuthorDelegate();
	private GenreDelegate gd= new GenreDelegate();
	private PurchaseDelegate pd= new PurchaseDelegate();
	private LoginDelegate ld= new LoginDelegate();
	private DelegateFactory() {
		super();
	}
	public static synchronized DelegateFactory getInstance() {
		if(df==null) {
			df=new DelegateFactory();
		}
		return df;
	}
	
	public FrontControllerDelegate getDelegate(String name) {
		switch(name) {
		case "books": return bd;
		case "authors": return ad;
		case "genres": return gd;
		case "login": return ld;
		case "purchases": return pd;
		default: return null;
		}
	}
}
