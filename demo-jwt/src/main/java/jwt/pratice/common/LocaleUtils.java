package jwt.pratice.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Class containing support for locales.  The original motivation for
 * development of this class is for use in a user inteface that provides
 * a selection box containing the list of supported locale codes and
 * their display names.  However, use of this class is not limited to
 * user interfaces.
 *
 */

public final class LocaleUtils {
    // intializing logger
    private static final Logger logger = Logger.getLogger(LocaleUtils.class);

    /**
     * This list contains list of locale supported by ExtJS.
     */
    private static final Set<String> EXTJS_SUPPORTED_LOCALES = new HashSet<>();


    // Since this is a utility class, prevent construction of an instance
    private LocaleUtils() {
    }

    /**
     * Simple class that holds a locale code and its display name.
     * The code is in one of the following forms:
     * <ul>
     * <li>language (example: en)</li>
     * <li>language_country (example: en_US)</li>
     * </ul>
     * The display name is in terms of a a particular, not necessarily
     * the same one that is represented by the corresponding code.
     */
    public static class LocaleInfo {

        // Members are public since this class functions as a struct
        public String code;
        public String displayName;

        public LocaleInfo(String iCode, String iDisplayName) {
            code = iCode;
            displayName = iDisplayName;
        }
    }

    // A mapping from language code to a collection of LocaleInfo objects
    // Once we know the supported locales, we cache them here
    private static Collection<Locale> supportedLocales = null;

    private static String[] supportedLangs = null;

    private static Locale[] translationLocales = null;

    /**
     * Initializes the static data members.
     */
    public static void initialize() {
        loadTranslationLocales();

        loadSupportedLangs();

        loadSupportedLocales();

    }


    /**
     * Given a locale, return the corresponding locale code.
     * The code will be in one of the following forms:
     * <ul>
     * <li>language (example: en)</li>
     * <li>language_country (example: en_US)</li>
     * </ul>
     *
     * @param iLocale The locale
     * @return the code
     */
    public static String formatLocaleCode(Locale iLocale) {
        String lang = iLocale.getLanguage();

        String code = null;
        if ("".equals(iLocale.getCountry())) {
            // Country not included
            code = iLocale.getLanguage();
        } else {
            // Country included
            code = iLocale.getLanguage() + '_' + iLocale.getCountry();
        }

        return code;
    }

    /**
     * Determine the default locale. The default locale is the first
     * local specified in the list for the property support_lang_code in
     * manu.properties
     *
     * @return default locale
     */
   /* public static Locale getDefaultLocale() {
        initialize();
        return translationLocales[0];
    }*/
    
    public static Locale getDefaultLocale() {
    	Locale locale = LocaleContextHolder.getLocale();
    	if(locale != null) {
    		System.out.println("country code="+locale.getCountry()+" lang code="+locale.getLanguage());
    		logger.info("Country code:{}"+locale.getCountry()+" language code:{}"+ locale.getLanguage());
    	} else {
    		System.out.println("Default English locale.....");
    		logger.info("Default English locale......");
    		locale = Locale.ENGLISH;
    	}
    	return locale;
    }

    /**
     * Return a collection of locale information for the locales that we
     * support.  Each Info object includes a locale code and its display
     * name in the specified locale (<code>iLocaleForDisplayName</code>).
     *
     * @param iLocaleForDisplayName
     * @return Collection of <code>LocaleInfo</code> objects
     */
    public static Collection<LocaleInfo> getSupportedLocaleInfo(Locale iLocaleForDisplayName) {

        // initialize if we haven't already
        initialize();

        Collection<LocaleInfo> resultCol = new ArrayList<LocaleInfo>(supportedLocales.size());

        // Loop through each supported locale
        Iterator<Locale> iter = supportedLocales.iterator();

        Iterator<Locale> sortedIter = sortSupportedLocales(iter);
        while (sortedIter.hasNext()) {
            Locale locale = sortedIter.next();

            String code = formatLocaleCode(locale);

            // Get the display name in terms of the specified locale
            String displayName = locale.getDisplayName(iLocaleForDisplayName);

            // Add to the collection
            LocaleInfo localInfo = new LocaleInfo(code, displayName);
            resultCol.add(localInfo);
        }

        return resultCol;
    }


    /**
     * Return a collection of locale information for the locales that we
     * support in the natural ordering.  Each Info object includes a
     * locale code and its display
     * name in the specified locale (<code>iLocaleForDisplayName</code>).
     *
     * @param iter iterator of supported locales
     * @return iterator of <code>LocaleInfo</code> objects
     */


    public static Iterator<Locale> sortSupportedLocales(Iterator<Locale> iter) {

        //hashtable to store the localeList
        Map<String, Locale> localeList = new HashMap<String, Locale>();

        ArrayList<String> codeList = new ArrayList<String>();

        ArrayList<Locale> returnList = new ArrayList<Locale>();

        // iterator to get the list of language codes to sort with
        while (iter.hasNext()) {
            Locale locale = iter.next();

            String code = formatLocaleCode(locale);
            codeList.add(code);
            localeList.put(code, locale);
        }
        // sort using the natural ordering
        Collections.sort(codeList);

        //from the code using the hashtable find the locale and reorder
        //locale into a list

        for (String code : codeList) {
            if (localeList.containsKey(code)) {
                Locale locale = localeList.get(code);
                returnList.add(locale);
            }
        }

        return returnList.iterator();
    }


    /**
     * This method will convert a locale string in the form of
     * <LanguageCode>_<CountryCode>_<Variant>.  Note that the
     * Language code is required but the country code or variant
     * might be missing.  A country code must exist if a variant
     * is specified.  If the locale cannot be created, null will be
     * returned.
     *
     * @param localeString The locale specified as a string
     * @return The locale object corresponding to the specified locale string
     */
    public static Locale parseLocale(String localeString) {
        // this routine will convert the localeString to a locale object.
        //  The expected format of the localeString is:
        //      <language>-<countrycode>-<varient>.  Note that only the
        //  language is required.

        Locale locale = null;

        if (localeString != null && localeString.length() != 0) {
            StringTokenizer st = new StringTokenizer(localeString, "_");

            String language = null;

            if (st.hasMoreTokens()) {
                language = st.nextToken();
            }

            String country = null;
            if (st.hasMoreTokens()) {
                country = st.nextToken();
            }

            String varient = null;
            if (st.hasMoreTokens()) {
                varient = st.nextToken();
            }

            // now create a locale
            if (language != null) {
                if (country == null) {
                    locale = new Locale(language, "");
                } else {
                    if (varient == null) {
                        locale = new Locale(language, country);
                    } else {
                        locale = new Locale(language, country, varient);
                    }
                }
            }
        }
        return locale;
    }

    /**
     * Returns the best supported locale from the specified enumeration
     * of locales.  This method will return the first specified locale
     * in the enumeration that matches in language to a supported locale.
     * It is assumed that if the language is supported, then all country
     * and varient variations are also supported.  If no language match
     * is found then english locale will be returned.
     *
     * @param locales - Enumeration of locale objects
     * @return Locale - the matched locale or default supported locale, if
     *         a match can't be made.
     */
    public static Locale getSupportedLocale(Enumeration<Locale> locales) {
        // initialize if we haven't already
        initialize();

        if (locales != null) {
            // loop over each locale in the enumeration and return it
            //  if it's language is in the supported lang list.
            while (locales.hasMoreElements()) {
                Locale desiredLocale = locales.nextElement();

                // Determine if this locale is for one of the supported languages
                for (String supportedLang : supportedLangs) {
                    // if the language mathes a locale we support, then return
                    //  the desired locale
                    if (desiredLocale.getLanguage().equals(supportedLang)) {
                        return desiredLocale;
                    }
                }
            }
        }

        // just return English Locale  //Md# 235713
        return Locale.ENGLISH;
    }


    /**
     * This method will convert return an array of supported language codes.
     * Each language code is a two letter abbreviation.
     * This method depends on a system property which is a comma delimited
     * string of supported language codes.
     */
    private static void loadSupportedLangs() {
        if (supportedLangs == null) {
            // since this class is static, make sure it's thread safe
            synchronized (LocaleUtils.class) {
                // check again to be sure
                if (supportedLangs == null) {

                    // build a unique set of language codes
                    Set<String> supportedLangsSet = new HashSet(translationLocales.length);

                    for (Locale translationLocale : translationLocales) {
                        supportedLangsSet.add(translationLocale.getLanguage());
                    }

                    // convert the unique set to an array of the supported languages
                    supportedLangs = new String[supportedLangsSet.size()];

                    int i = 0;
                    for (String aSupportedLangsSet : supportedLangsSet) {
                        supportedLangs[i++] = aSupportedLangsSet;
                    }
                }
            }
        }
    }


    private static void loadTranslationLocales() {

        if (translationLocales == null) {
            synchronized (LocaleUtils.class) {
                if (translationLocales == null) {
                    String delimitedLangCodes = "en";

                    StringTokenizer st = new StringTokenizer(delimitedLangCodes, ",");

                    translationLocales = new Locale[st.countTokens()];

                    for (int i = 0; i < translationLocales.length; i++) {
                        translationLocales[i] = parseLocale(st.nextToken());
                    }
                }
            }
        }
    }


    /**
     * If the <code>supportedLocales</code> collection is not already
     * set, determine the locales that we support.  Store them into the
     * <code>supportedLocales</code> collection.
     */
    private static void loadSupportedLocales() {

        if (supportedLocales == null) {
            synchronized (LocaleUtils.class) {

                if (supportedLocales == null) {
                    supportedLocales = new ArrayList<Locale>();

                    logger.info("About to get all Java locales");

                    // Get all the available locales
                    // This call is very slow.  This is why we cache the supported locales
                    Locale[] locales = Locale.getAvailableLocales();
                    logger.info("Got locales:"+ locales.length);

                    for (Locale locale : locales) {
                        String lang = locale.getLanguage();

                        // Determine if this locale is for one of the supported languages
                        for (String supportedLang : supportedLangs) {
                            // skip over any locales with a country code
                            if (!lang.equals(supportedLang)) {
                                continue;
                            }

                            // This locale is supported by Manugistics
                            supportedLocales.add(locale);
                        }
                    }

                    logger.info("We support locales: "+ supportedLocales.size());
                }
            }
        }
    }


    public static Locale mapToTranslationLocale(Locale supportedLocale) {
        // initialize if we haven't already
        initialize();

        Locale translationLocale = null;

        // look for an exact match first (i.e. same lang, country, and varient)
        for (Locale translationLocale1 : translationLocales) {
            if (supportedLocale.getLanguage().equalsIgnoreCase(translationLocale1.getLanguage()) &&
                    supportedLocale.getCountry().equalsIgnoreCase(translationLocale1.getCountry()) &&
                    supportedLocale.getVariant().equalsIgnoreCase(translationLocale1.getVariant())) {
                translationLocale = translationLocale1;
                break;
            }
        }

        if (translationLocale == null) {
            // look for a locale with matching lang and country only
            for (Locale translationLocale1 : translationLocales) {
                if (supportedLocale.getLanguage().equalsIgnoreCase(translationLocale1.getLanguage()) &&
                        supportedLocale.getCountry().equalsIgnoreCase(translationLocale1.getCountry())) {
                    translationLocale = translationLocale1;
                    break;
                }
            }
        }

        if (translationLocale == null) {

            // look for a locale with matching lang
            for (Locale translationLocale1 : translationLocales) {

                if (supportedLocale.getLanguage().equalsIgnoreCase(translationLocale1.getLanguage())) {
                    translationLocale = translationLocale1;
                    break;
                }
            }
        }

        // no match at all so just return the first (default) real locale
        if (translationLocale == null) {
            translationLocale = translationLocales[0];
        }

        return translationLocale;
    }
}

