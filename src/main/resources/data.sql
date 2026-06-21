-- =========================================
-- LiveMatch — initial seed data
-- Idempotent: only inserts if tables are empty
-- =========================================

-- Teams (with custom codes for UK home nations)
INSERT INTO teams (name, short_name, country)
SELECT v.name, v.short_name, v.country
FROM (VALUES
          -- Spain (ES)
          ('FC Barcelona', 'FCB', 'ES'),
          ('Real Madrid', 'RMA', 'ES'),
          ('Atlético Madrid', 'ATM', 'ES'),
          ('Athletic Bilbao', 'ATH', 'ES'),
          -- England (ENG — custom for UK home nation)
          ('Manchester City', 'MCI', 'ENG'),
          ('Liverpool', 'LIV', 'ENG'),
          ('Arsenal', 'ARS', 'ENG'),
          ('Manchester United', 'MUN', 'ENG'),
          ('Chelsea', 'CHE', 'ENG'),
          -- Scotland (SCT)
          ('Celtic', 'CEL', 'SCT'),
          ('Rangers', 'RAN', 'SCT'),
          -- Wales (WLS)
          ('Cardiff City', 'CDF', 'WLS'),
          -- Northern Ireland (NIR)
          ('Linfield', 'LIN', 'NIR'),
          -- Germany (DE)
          ('Bayern Munich', 'BAY', 'DE'),
          ('Borussia Dortmund', 'BVB', 'DE'),
          ('RB Leipzig', 'RBL', 'DE'),
          -- France (FR)
          ('Paris Saint-Germain', 'PSG', 'FR'),
          ('Olympique Marseille', 'OM', 'FR'),
          -- Italy (IT)
          ('Juventus', 'JUV', 'IT'),
          ('AC Milan', 'MIL', 'IT'),
          ('Inter Milan', 'INT', 'IT'),
          -- Poland (PL)
          ('Legia Warszawa', 'LEG', 'PL'),
          ('Lech Poznań', 'LEC', 'PL'),
          -- Portugal (PT)
          ('Benfica', 'BEN', 'PT'),
          ('FC Porto', 'POR', 'PT'),
          -- Netherlands (NL)
          ('Ajax Amsterdam', 'AJA', 'NL'),
         ---------------------------------------------------------
         ---------------------------------------------------------
         --                   National Teams                    --
         ---------------------------------------------------------
         ---------------------------------------------------------
          -- South America (CONMEBOL)
          (NULL, 'ARG', 'AR'),  -- Argentina
          (NULL, 'BRA', 'BR'),  -- Brazil
          (NULL, 'URU', 'UY'),  -- Uruguay
          (NULL, 'COL', 'CO'),  -- Colombia
          (NULL, 'CHI', 'CL'),  -- Chile
          (NULL, 'ECU', 'EC'),  -- Ecuador
          (NULL, 'PAR', 'PY'),  -- Paraguay
          (NULL, 'PER', 'PE'),  -- Peru
          -- Europe (UEFA)
          (NULL, 'GER', 'DE'),  -- Germany
          (NULL, 'FRA', 'FR'),  -- France
          (NULL, 'ESP', 'ES'),  -- Spain
          (NULL, 'ITA', 'IT'),  -- Italy
          (NULL, 'NED', 'NL'),  -- Netherlands
          (NULL, 'BEL', 'BE'),  -- Belgium
          (NULL, 'POR', 'PT'),  -- Portugal
          (NULL, 'CRO', 'HR'),  -- Croatia
          (NULL, 'POL', 'PL'),  -- Poland
          (NULL, 'SUI', 'CH'),  -- Switzerland
          (NULL, 'DEN', 'DK'),  -- Denmark
          (NULL, 'SWE', 'SE'),  -- Sweden
          (NULL, 'NOR', 'NO'),  -- Norway
          (NULL, 'SRB', 'RS'),  -- Serbia
          (NULL, 'AUT', 'AT'),  -- Austria
          (NULL, 'CZE', 'CZ'),  -- Czechia
          (NULL, 'UKR', 'UA'),  -- Ukraine
          (NULL, 'TUR', 'TR'),  -- Turkey
          (NULL, 'BIH', 'BA'),  -- Bosnia and Herzegovina
          -- UK home nations (custom codes for both shortName and country)
          (NULL, 'ENG', 'ENG'), -- England
          (NULL, 'SCO', 'SCT'), -- Scotland
          (NULL, 'WAL', 'WLS'), -- Wales
          (NULL, 'NIR', 'NIR'), -- Northern Ireland
          -- North/Central America (CONCACAF)
          (NULL, 'USA', 'US'),  -- USA
          (NULL, 'MEX', 'MX'),  -- Mexico
          (NULL, 'CAN', 'CA'),  -- Canada
          (NULL, 'HTI', 'HT'),  -- Haiti
          (NULL, 'CUW', 'CW'),  -- Curacao
          (NULL, 'CRC', 'CR'),  -- Costa Rica
          (NULL, 'PAN', 'PA'),  -- Panama
          -- Asia (AFC)
          (NULL, 'JPN', 'JP'),  -- Japan
          (NULL, 'KOR', 'KR'),  -- South Korea
          (NULL, 'AUS', 'AU'),  -- Australia
          (NULL, 'IRN', 'IR'),  -- Iran
          (NULL, 'IRQ', 'IQ'),  -- Iraq
          (NULL, 'QAT', 'QA'),  -- Qatar
          (NULL, 'KSA', 'SA'),  -- Saudi Arabia
          (NULL, 'JOR', 'JO'),  -- Jordan
          (NULL, 'UZB', 'UZ'),  -- Uzbekistan
          -- Africa (CAF)
          (NULL, 'SEN', 'SN'),  -- Senegal
          (NULL, 'MAR', 'MA'),  -- Morocco
          (NULL, 'TUN', 'TN'),  -- Tunisia
          (NULL, 'CMR', 'CM'),  -- Cameroon
          (NULL, 'NGA', 'NG'),  -- Nigeria
          (NULL, 'GHA', 'GH'),  -- Ghana
          (NULL, 'EGY', 'EG'),  -- Egypt
          (NULL, 'CPV', 'CV'),  -- Cabo Verde
          (NULL, 'CIV', 'CI'),  -- Ivory Coast
          (NULL, 'RSA', 'ZA'),  -- South Africa
          (NULL, 'DZA', 'DZ'),  -- Algeria
          (NULL, 'COD', 'CD'),  -- DR Congo
          -- Oceania (OFC)
          (NULL, 'NZL', 'NZ')   -- New Zealand

     ) AS v(name, short_name, country)
WHERE NOT EXISTS (SELECT 1 FROM teams);

-- Sample matches (one of each status for demo)
INSERT INTO matches (home_team_id, away_team_id, start_time, status, home_score, away_score)
SELECT * FROM (VALUES
                   -- LIVE match (started 30 min ago)
                   (
                       (SELECT id FROM teams WHERE short_name = 'DZA'),
                       (SELECT id FROM teams WHERE short_name = 'BIH'),
                       CURRENT_TIMESTAMP - INTERVAL '30 minutes',
                       'LIVE',
                       1,
                       0
                   ),
                   -- SCHEDULED match (in 3 days)
                   (
                       (SELECT id FROM teams WHERE short_name = 'BEL'),
                       (SELECT id FROM teams WHERE short_name = 'PER'),
                       CURRENT_TIMESTAMP + INTERVAL '3 days',
                       'SCHEDULED',
                       0,
                       0
                   ),
                   -- SCHEDULED match (next week)
                   (
                       (SELECT id FROM teams WHERE short_name = 'CUW'),
                       (SELECT id FROM teams WHERE short_name = 'QAT'),
                       CURRENT_TIMESTAMP + INTERVAL '7 days',
                       'SCHEDULED',
                       0,
                       0
                   ),
                   -- FINISHED match (last week)
                   (
                       (SELECT id FROM teams WHERE short_name = 'SCO'),
                       (SELECT id FROM teams WHERE short_name = 'NOR'),
                       CURRENT_TIMESTAMP - INTERVAL '7 days',
                       'FINISHED',
                       3,
                       1
                   )
              ) AS v(home_team_id, away_team_id, start_time, status, home_score, away_score)
WHERE NOT EXISTS (SELECT 1 FROM matches);