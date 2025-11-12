package com.finora.domain.model

/**
 * Known merchants by region for smart AI categorization.
 * Used by CategoryPredictor to improve accuracy.
 */
object MerchantsByRegion {
    
    /**
     * UK Merchants
     */
    val UK_MERCHANTS = mapOf(
        // Food & Groceries
        "tesco" to ExpenseCategory.FOOD,
        "sainsburys" to ExpenseCategory.FOOD,
        "sainsbury's" to ExpenseCategory.FOOD,
        "asda" to ExpenseCategory.FOOD,
        "morrisons" to ExpenseCategory.FOOD,
        "waitrose" to ExpenseCategory.FOOD,
        "aldi" to ExpenseCategory.FOOD,
        "lidl" to ExpenseCategory.FOOD,
        "co-op" to ExpenseCategory.FOOD,
        "coop" to ExpenseCategory.FOOD,
        "marks & spencer" to ExpenseCategory.FOOD,
        "m&s" to ExpenseCategory.FOOD,
        
        // Restaurants & Takeaway
        "nandos" to ExpenseCategory.FOOD,
        "nando's" to ExpenseCategory.FOOD,
        "pret a manger" to ExpenseCategory.FOOD,
        "pret" to ExpenseCategory.FOOD,
        "greggs" to ExpenseCategory.FOOD,
        "costa" to ExpenseCategory.FOOD,
        "starbucks" to ExpenseCategory.FOOD,
        "wagamama" to ExpenseCategory.FOOD,
        "pizza express" to ExpenseCategory.FOOD,
        "deliveroo" to ExpenseCategory.FOOD,
        "uber eats" to ExpenseCategory.FOOD,
        "just eat" to ExpenseCategory.FOOD,
        
        // Transport
        "tfl" to ExpenseCategory.TRANSPORTATION,
        "transport for london" to ExpenseCategory.TRANSPORTATION,
        "uber" to ExpenseCategory.TRANSPORTATION,
        "national rail" to ExpenseCategory.TRANSPORTATION,
        "trainline" to ExpenseCategory.TRANSPORTATION,
        "gwr" to ExpenseCategory.TRANSPORTATION,
        "lner" to ExpenseCategory.TRANSPORTATION,
        "avanti" to ExpenseCategory.TRANSPORTATION,
        "bp" to ExpenseCategory.TRANSPORTATION,
        "shell" to ExpenseCategory.TRANSPORTATION,
        "esso" to ExpenseCategory.TRANSPORTATION,
        "texaco" to ExpenseCategory.TRANSPORTATION,
        
        // Shopping
        "amazon" to ExpenseCategory.SHOPPING,
        "ebay" to ExpenseCategory.SHOPPING,
        "argos" to ExpenseCategory.SHOPPING,
        "john lewis" to ExpenseCategory.SHOPPING,
        "next" to ExpenseCategory.SHOPPING,
        "primark" to ExpenseCategory.SHOPPING,
        "zara" to ExpenseCategory.SHOPPING,
        "h&m" to ExpenseCategory.SHOPPING,
        "boots" to ExpenseCategory.HEALTHCARE,
        "superdrug" to ExpenseCategory.HEALTHCARE,
        
        // Entertainment
        "netflix" to ExpenseCategory.ENTERTAINMENT,
        "spotify" to ExpenseCategory.ENTERTAINMENT,
        "disney+" to ExpenseCategory.ENTERTAINMENT,
        "amazon prime" to ExpenseCategory.ENTERTAINMENT,
        "sky" to ExpenseCategory.ENTERTAINMENT,
        "odeon" to ExpenseCategory.ENTERTAINMENT,
        "vue" to ExpenseCategory.ENTERTAINMENT,
        "cineworld" to ExpenseCategory.ENTERTAINMENT,
        
        // Utilities
        "british gas" to ExpenseCategory.UTILITIES,
        "edf" to ExpenseCategory.UTILITIES,
        "eon" to ExpenseCategory.UTILITIES,
        "ee" to ExpenseCategory.UTILITIES,
        "vodafone" to ExpenseCategory.UTILITIES,
        "o2" to ExpenseCategory.UTILITIES,
        "three" to ExpenseCategory.UTILITIES,
        "bt" to ExpenseCategory.UTILITIES,
        "virgin media" to ExpenseCategory.UTILITIES
    )
    
    /**
     * Europe (Continental) Merchants
     */
    val EUROPE_MERCHANTS = mapOf(
        // Food & Groceries
        "carrefour" to ExpenseCategory.FOOD,
        "lidl" to ExpenseCategory.FOOD,
        "aldi" to ExpenseCategory.FOOD,
        "rewe" to ExpenseCategory.FOOD,
        "edeka" to ExpenseCategory.FOOD,
        "auchan" to ExpenseCategory.FOOD,
        "mercadona" to ExpenseCategory.FOOD,
        "albert heijn" to ExpenseCategory.FOOD,
        "jumbo" to ExpenseCategory.FOOD,
        
        // Restaurants
        "starbucks" to ExpenseCategory.FOOD,
        "mcdonald's" to ExpenseCategory.FOOD,
        "burger king" to ExpenseCategory.FOOD,
        "kfc" to ExpenseCategory.FOOD,
        "deliveroo" to ExpenseCategory.FOOD,
        "uber eats" to ExpenseCategory.FOOD,
        "lieferando" to ExpenseCategory.FOOD,
        "just eat" to ExpenseCategory.FOOD,
        
        // Transport
        "uber" to ExpenseCategory.TRANSPORTATION,
        "bolt" to ExpenseCategory.TRANSPORTATION,
        "sncf" to ExpenseCategory.TRANSPORTATION,
        "deutsche bahn" to ExpenseCategory.TRANSPORTATION,
        "db" to ExpenseCategory.TRANSPORTATION,
        "renfe" to ExpenseCategory.TRANSPORTATION,
        "trenitalia" to ExpenseCategory.TRANSPORTATION,
        "shell" to ExpenseCategory.TRANSPORTATION,
        "total" to ExpenseCategory.TRANSPORTATION,
        "bp" to ExpenseCategory.TRANSPORTATION,
        
        // Shopping
        "amazon" to ExpenseCategory.SHOPPING,
        "zalando" to ExpenseCategory.SHOPPING,
        "h&m" to ExpenseCategory.SHOPPING,
        "zara" to ExpenseCategory.SHOPPING,
        "ikea" to ExpenseCategory.SHOPPING,
        "decathlon" to ExpenseCategory.SHOPPING,
        
        // Entertainment
        "netflix" to ExpenseCategory.ENTERTAINMENT,
        "spotify" to ExpenseCategory.ENTERTAINMENT,
        "disney+" to ExpenseCategory.ENTERTAINMENT,
        "amazon prime" to ExpenseCategory.ENTERTAINMENT
    )
    
    /**
     * North America Merchants
     */
    val NORTH_AMERICA_MERCHANTS = mapOf(
        // Food & Groceries
        "walmart" to ExpenseCategory.FOOD,
        "target" to ExpenseCategory.FOOD,
        "kroger" to ExpenseCategory.FOOD,
        "costco" to ExpenseCategory.FOOD,
        "whole foods" to ExpenseCategory.FOOD,
        "trader joe's" to ExpenseCategory.FOOD,
        "safeway" to ExpenseCategory.FOOD,
        
        // Restaurants
        "mcdonald's" to ExpenseCategory.FOOD,
        "starbucks" to ExpenseCategory.FOOD,
        "chipotle" to ExpenseCategory.FOOD,
        "subway" to ExpenseCategory.FOOD,
        "doordash" to ExpenseCategory.FOOD,
        "uber eats" to ExpenseCategory.FOOD,
        "grubhub" to ExpenseCategory.FOOD,
        
        // Transport
        "uber" to ExpenseCategory.TRANSPORTATION,
        "lyft" to ExpenseCategory.TRANSPORTATION,
        "shell" to ExpenseCategory.TRANSPORTATION,
        "chevron" to ExpenseCategory.TRANSPORTATION,
        "exxon" to ExpenseCategory.TRANSPORTATION,
        
        // Shopping
        "amazon" to ExpenseCategory.SHOPPING,
        "ebay" to ExpenseCategory.SHOPPING,
        "best buy" to ExpenseCategory.SHOPPING,
        "macy's" to ExpenseCategory.SHOPPING,
        
        // Entertainment
        "netflix" to ExpenseCategory.ENTERTAINMENT,
        "spotify" to ExpenseCategory.ENTERTAINMENT,
        "hulu" to ExpenseCategory.ENTERTAINMENT,
        "disney+" to ExpenseCategory.ENTERTAINMENT
    )
    
    /**
     * South America Merchants (Brazil focus)
     */
    val SOUTH_AMERICA_MERCHANTS = mapOf(
        "ifood" to ExpenseCategory.FOOD,
        "rappi" to ExpenseCategory.FOOD,
        "uber eats" to ExpenseCategory.FOOD,
        "pao de acucar" to ExpenseCategory.FOOD,
        "carrefour" to ExpenseCategory.FOOD,
        "extra" to ExpenseCategory.FOOD,
        "uber" to ExpenseCategory.TRANSPORTATION,
        "99" to ExpenseCategory.TRANSPORTATION,
        "nubank" to ExpenseCategory.UTILITIES,
        "netflix" to ExpenseCategory.ENTERTAINMENT,
        "spotify" to ExpenseCategory.ENTERTAINMENT
    )
    
    /**
     * Get merchants for a specific region.
     */
    fun getMerchantsForRegion(region: Region): Map<String, ExpenseCategory> {
        return when (region) {
            Region.UK -> UK_MERCHANTS
            Region.EUROPE -> EUROPE_MERCHANTS
            Region.NORTH_AMERICA -> NORTH_AMERICA_MERCHANTS
            Region.SOUTH_AMERICA -> SOUTH_AMERICA_MERCHANTS
            Region.ASIA, Region.OCEANIA -> EUROPE_MERCHANTS // Fallback to Europe
        }
    }
    
    /**
     * Search for merchant across all regions (fallback).
     */
    fun searchMerchantGlobally(merchant: String): ExpenseCategory? {
        val normalized = merchant.lowercase().trim()
        
        val allMerchants = UK_MERCHANTS + EUROPE_MERCHANTS + 
                          NORTH_AMERICA_MERCHANTS + SOUTH_AMERICA_MERCHANTS
        
        return allMerchants.entries
            .find { normalized.contains(it.key) || it.key.contains(normalized) }
            ?.value
    }
}
