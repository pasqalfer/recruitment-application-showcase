import json
import urllib.request
import urllib.parse

def read_json(path):
    with open(path) as json_file:
        return json.load(json_file)

def write_json(file,obj):
# Using a JSON string
    with open(file, 'w') as outfile:
        outfile.write(json.dumps(obj))


def map_user_properties(data, property_builder):
    a = {}
    for d in data:
        person = d['person_id']
        if person not in a.keys():
            a[person] = []
        a[person].append(property_builder(d))
    return a


def find(lst, predicate):
    for e in lst:
        found = predicate(e)
        if found:
            return e

def do_request(url,params):
    contents = urllib.request.urlopen(url).read()
    print(contents)

users = {'applicants': [], 'recruiters': []}

user_data = read_json('./schema_old/person.json')

competenceTypes = []

competenceTypes = read_json('schema_old/competences.json')

# format the
usr_availability = map_user_properties(
    read_json('schema_old/availability.json'),
    lambda p: {'dateFrom': p['from_date'], 'dateTo': p['to_date']})

usr_competences = map_user_properties(
    read_json('schema_old/competence_profile.json'),
    lambda p: {'yearsExperience': p['years_of_experience'],
               'competence': find(competenceTypes, lambda e: e['competence_id'] == p['competence_id'])['name']})

for user in user_data:
    person = user['person_id']
    role = user['role_id']
    if role == 1:
        users['recruiters'].append(user)
    elif role == 2:
        user['application'] = {'availability': usr_availability.get(person), 'competences': usr_competences.get(person)}
        users['applicants'].append(user)


# write formated data to file
write_json("schema/applicants.json", users.get('applicants'))
write_json("schema/recruiters.json", users.get('recruiters'))
